package world.jdl.gateway.observables;

import com.google.gson.annotations.SerializedName;
import world.jdl.observe.Observer;
import world.jdl.structure.guild.Guild;
import world.jdl.structure.user.SelfUser;
import world.jdl.structure.user.User;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class ReadyGatewayObservable implements Observer.Observable
{
    @SerializedName("v")
    private int version;
    @SerializedName("session_id")
    private String sessionId;
    @SerializedName("resume_gateway_url")
    private String resumeGatewayUrl;
    private SelfUser user;
    private final List<Guild> guilds = new LinkedList<>();

    public int getVersion()
    {
        return version;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public String getResumeGatewayUrl()
    {
        return resumeGatewayUrl;
    }

    public SelfUser getUser()
    {
        return user;
    }

    public List<Guild> getGuilds()
    {
        return guilds;
    }
}
