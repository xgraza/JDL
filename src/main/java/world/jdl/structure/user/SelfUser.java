package world.jdl.structure.user;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import world.jdl.rest.Endpoints;
import world.jdl.rest.RESTAction;
import world.jdl.rest.RESTClient;

/**
 * @author xgraza
 * @since 9/10/25
 */
public final class SelfUser extends User
{
    @SerializedName("mfa_enabled")
    private boolean mfaEnabled;
    private String email;
    private boolean verified;

    /**
     * Modifies this current users username
     * @param username the new username
     * @return a {@link RESTAction} that provides a {@link SelfUser} on completion
     */
    public RESTAction<SelfUser> setUsername(final String username)
    {
        if (!isValidUsername(username))
        {
            throw new RuntimeException("username does not follow constraints\n" +
                    "read https://discord.com/developers/docs/resources/user#usernames-and-nicknames");
        }
        final JsonObject object = new JsonObject();
        object.addProperty("username", username);
        return RESTClient.getInstance().patch(Endpoints.Users.MODIFY_USER, object);
    }

    /**
     * If 2FA is enabled on this account
     * @return if 2fa is enabled
     */
    public boolean isMfaEnabled()
    {
        return mfaEnabled;
    }

    public String getEmail()
    {
        return email;
    }

    /**
     * If this users email has been verified
     * @return verified
     */
    public boolean isVerified()
    {
        return verified;
    }
}
