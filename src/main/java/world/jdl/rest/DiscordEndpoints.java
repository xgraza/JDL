package world.jdl.rest;

import world.jdl.structure.user.User;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class DiscordEndpoints
{
    public interface Channels
    {
        Endpoint<Object> CREATE_MESSAGE = new Endpoint<>("POST", "channels/%s/messages", Object.class);
    }

    public interface Users
    {
        Endpoint<User> CURRENT_USER = new Endpoint<>("GET", "users/@me", User.class);

//        // GET
//        String CURRENT_USER = "users/@me";
//        String USER = "users/%s";
//        String CURRENT_GUILDS = "users/@me/guilds";
//        String CURRENT_GUILD_MEMBER = "users/@me/guilds/%s/member";
//        String USER_CONNECTIONS = "users/@me/connections";
//        String CURRENT_USER_APP_ROLE = "users/@me/applications/%s/role-connection";
//
//        // POST
//        String CREATE_DM = "users/@me/channels"; // create single, create multi-user DM
//
//        // PUT
//        String UPDATE_CURRENT_USER_APP_ROLE = "users/@me/applications/%s/role-connection";
//
//        // PATCH
//        String MODIFY_USER = "users/@me";
//
//        // DELETE
//        String LEAVE_GUILD = "users/@me/guilds/%s";
    }

    public record Endpoint<T>(String method, String route, Class<T> responseType)
        {
            public Endpoint<T> with(final Object... fmt)
            {
                return new Endpoint<>(method, String.format(route, fmt), responseType);
            }
        }
}
