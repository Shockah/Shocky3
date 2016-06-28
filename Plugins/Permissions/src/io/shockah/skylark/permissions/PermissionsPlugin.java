package io.shockah.skylark.permissions;

import io.shockah.skylark.DatabaseManager;
import io.shockah.skylark.UnexpectedException;
import io.shockah.skylark.ident.IdentMethod;
import io.shockah.skylark.ident.IdentPlugin;
import io.shockah.skylark.permissions.db.UserGroup;
import io.shockah.skylark.permissions.db.UserGroupIdent;
import io.shockah.skylark.plugin.Plugin;
import io.shockah.skylark.plugin.PluginManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.pircbotx.User;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

public class PermissionsPlugin extends Plugin {
	@Dependency
	protected IdentPlugin identPlugin;
	
	public PermissionsPlugin(PluginManager manager, Info info) {
		super(manager, info);
	}
	
	public List<UserGroup> getUserGroups(User user) {
		Map<IdentMethod, String> idents = identPlugin.getIdentsForUser(user);
		List<UserGroup> groups = new ArrayList<>();
		for (Map.Entry<IdentMethod, String> entry : idents.entrySet()) {
			List<UserGroup> groupsForIdent = getGroupsForIdent(entry.getKey(), entry.getValue());
			for (UserGroup group : groupsForIdent) {
				if (!groups.contains(group))
					groups.add(group);
			}
		}
		return groups;
	}
	
	public List<UserGroup> getGroupsForIdent(IdentMethod method, String identString) {
		try {
			DatabaseManager databaseManager = manager.app.databaseManager; 
			
			QueryBuilder<UserGroupIdent, Integer> qbIdent = databaseManager.getDao(UserGroupIdent.class, Integer.class).queryBuilder();
			qbIdent.where().eq(UserGroupIdent.METHOD_COLUMN, method.prefix)
				.and().raw(String.format("? REGEXP %s", UserGroupIdent.IDENT_PATTERN_COLUMN), new SelectArg(SqlType.STRING, identString));
			
			QueryBuilder<UserGroup, Integer> qbGroup = databaseManager.getDao(UserGroup.class, Integer.class).queryBuilder();
			
			return qbGroup.join(qbIdent).query();
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
	}
	
	public boolean permissionGranted(User user, String permission) {
		for (UserGroup group : getUserGroups(user)) {
			if (group.permissionGranted(permission))
				return true;
		}
		return false;
	}
	
	public boolean permissionGranted(IdentMethod method, String identString, String permission) {
		for (UserGroup group : getGroupsForIdent(method, identString)) {
			if (group.permissionGranted(permission))
				return true;
		}
		return false;
	}
}