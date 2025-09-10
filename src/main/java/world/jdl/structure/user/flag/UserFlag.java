package world.jdl.structure.user.flag;

import world.jdl.structure.flag.IBitFlag;

/**
 * @author xgraza
 * @since 9/10/25
 * @see <a href="https://discord.com/developers/docs/resources/user#user-object-user-flags">Discord Documentation</a>
 */
public enum UserFlag implements IBitFlag
{
    STAFF(1 << 0),
    PARTNER(1 << 1),
    HYPESQUAD(1 << 2),
    BUG_HUNTER_LEVEL_1(1 << 3),
    HYPESQUAD_BRAVERY(1 << 6),
    HYPESQUAD_BRILLIANCE(1 << 7),
    HYPESQUAD_BALANCE(1 << 8),
    EARLY_NITRO_SUPPORTER(1 << 9),
    TEAM_PSEDUO_USER(1 << 10),
    BUG_HUNTER_LEVEL_2(1 << 14),
    VERIFIED_BOT(1 << 16),
    EARLY_VERIFIED_DEVELOPER(1 << 17),
    CERTIFIED_DEVELOPER(1 << 18),
    BOT_HTTP_INTERACTIONS(1 << 19),
    ACTIVE_DEVELOPER(1 << 22);

    private final int bit;

    UserFlag(final int bit)
    {
        this.bit = bit;
    }

    @Override
    public int getValue()
    {
        return bit;
    }
}
