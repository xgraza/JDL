package world.jdl.gateway.event.events;

import com.google.gson.annotations.SerializedName;
import world.jdl.gateway.event.IGatewayEvent;
import world.jdl.listener.IEventListener;
import world.jdl.structure.guild.Guild;
import world.jdl.structure.user.User;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 9/8/25
 */
// {
//  "t": "READY",
//  "s": 1,
//  "op": 0,
//  "d": {
//    "v": 10,
//    "user_settings": {},
//    "user": {
//      "verified": true,
//      "username": "testing shit",
//      "primary_guild": null,
//      "mfa_enabled": false,
//      "id": "1158429530684412015",
//      "global_name": null,
//      "flags": 0,
//      "email": null,
//      "discriminator": "0128",
//      "clan": null,
//      "bot": true,
//      "avatar": null
//    },
//    "session_type": "normal",
//    "session_id": "92fb8cfb95550a23a74d01488c3ab382",
//    "resume_gateway_url": "wss://gateway-us-east1-b.discord.gg",
//    "relationships": [],
//    "private_channels": [],
//    "presences": [],
//    "guilds": [
//      {
//        "unavailable": true,
//        "id": "1145942053327482880"
//      }
//    ],
//    "guild_join_requests": [],
//    "geo_ordered_rtc_regions": [
//      "us-central",
//      "us-east",
//      "atlanta",
//      "newark",
//      "us-south"
//    ],
//    "game_relationships": [],
//    "auth": {},
//    "application": {
//      "id": "1158429530684412015",
//      "flags": 8945664
//    },
//    "_trace": [
//      "[\"gateway-prd-arm-us-east1-b-9jpb\",{\"micros\":114012,\"calls\":[\"id_created\",{\"micros\":515,\"calls\":[]},\"session_lookup_time\",{\"micros\":784,\"calls\":[]},\"session_lookup_finished\",{\"micros\":10,\"calls\":[]},\"discord-sessions-prd-2-76\",{\"micros\":111779,\"calls\":[\"start_session\",{\"micros\":95040,\"calls\":[\"discord-api-rpc-7d8c7f9984-jtwnn\",{\"micros\":56635,\"calls\":[\"get_user\",{\"micros\":6795},\"get_guilds\",{\"micros\":5667},\"send_scheduled_deletion_message\",{\"micros\":12},\"guild_join_requests\",{\"micros\":3},\"authorized_ip_coro\",{\"micros\":16},\"pending_payments\",{\"micros\":22493},\"apex_experiments\",{\"micros\":3170},\"user_activities\",{\"micros\":6},\"played_application_ids\",{\"micros\":3}]}]},\"starting_guild_connect\",{\"micros\":30,\"calls\":[]},\"presence_started\",{\"micros\":2279,\"calls\":[]},\"guilds_started\",{\"micros\":57,\"calls\":[]},\"lobbies_started\",{\"micros\":1,\"calls\":[]},\"guilds_connect\",{\"micros\":1,\"calls\":[]},\"presence_connect\",{\"micros\":14349,\"calls\":[]},\"connect_finished\",{\"micros\":14352,\"calls\":[]},\"build_ready\",{\"micros\":18,\"calls\":[]},\"clean_ready\",{\"micros\":0,\"calls\":[]},\"optimize_ready\",{\"micros\":1,\"calls\":[]},\"split_ready\",{\"micros\":0,\"calls\":[]}]}]}]"
//    ]
//  }
//}
public final class ReadyGatewayEvent implements IGatewayEvent
{
    @SerializedName("v")
    private int version;
    @SerializedName("session_id")
    private String sessionId;
    @SerializedName("resume_gateway_url")
    private String resumeGatewayUrl;
    private User user;
    private final List<Guild> guilds = new LinkedList<>();

    @Override
    public void handle(final IEventListener listener)
    {
        listener.onReady(this);
    }

    public User getUser()
    {
        return user;
    }

    public List<Guild> getGuilds()
    {
        return guilds;
    }
}
