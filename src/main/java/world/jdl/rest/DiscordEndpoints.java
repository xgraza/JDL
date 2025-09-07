package world.jdl.rest;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class DiscordEndpoints
{
    public interface User
    {
        Endpoint<Object> CURRENT_USER = new Endpoint<>("GET", "users/@me", null);

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

    public record Endpoint<T>(String method, String endpoint, Class<T> responseType)
        {
            public Endpoint<T> with(final Object... fmt)
            {
                return new Endpoint<>(method, String.format(endpoint, fmt), responseType);
            }
        }
}
