package fr.xephi.authme.message;

public enum MessageKey
{
  DENIED_COMMAND("denied_command", new String[0]),  SAME_IP_ONLINE("same_ip_online", new String[0]),  DENIED_CHAT("denied_chat", new String[0]),  KICK_ANTIBOT("kick_antibot", new String[0]),  UNKNOWN_USER("unknown_user", new String[0]),  NOT_LOGGED_IN("not_logged_in", new String[0]),  USAGE_LOGIN("usage_log", new String[0]),  WRONG_PASSWORD("wrong_pwd", new String[0]),  UNREGISTERED_SUCCESS("unregistered", new String[0]),  REGISTRATION_DISABLED("reg_disabled", new String[0]),  SESSION_RECONNECTION("valid_session", new String[0]),  LOGIN_SUCCESS("login", new String[0]),  ACCOUNT_NOT_ACTIVATED("vb_nonActiv", new String[0]),  NAME_ALREADY_REGISTERED("user_regged", new String[0]),  NO_PERMISSION("no_perm", new String[0]),  ERROR("error", new String[0]),  LOGIN_MESSAGE("login_msg", new String[0]),  REGISTER_MESSAGE("reg_msg", new String[0]),  MAX_REGISTER_EXCEEDED("max_reg", new String[] { "%max_acc", "%reg_count", "%reg_names" }),  USAGE_REGISTER("usage_reg", new String[0]),  USAGE_UNREGISTER("usage_unreg", new String[0]),  PASSWORD_CHANGED_SUCCESS("pwd_changed", new String[0]),  PASSWORD_MATCH_ERROR("password_error", new String[0]),  PASSWORD_IS_USERNAME_ERROR("password_error_nick", new String[0]),  PASSWORD_UNSAFE_ERROR("password_error_unsafe", new String[0]),  PASSWORD_CHARACTERS_ERROR("password_error_chars", new String[] { "REG_EX" }),  SESSION_EXPIRED("invalid_session", new String[0]),  MUST_REGISTER_MESSAGE("reg_only", new String[0]),  ALREADY_LOGGED_IN_ERROR("logged_in", new String[0]),  LOGOUT_SUCCESS("logout", new String[0]),  USERNAME_ALREADY_ONLINE_ERROR("same_nick", new String[0]),  REGISTER_SUCCESS("registered", new String[0]),  INVALID_PASSWORD_LENGTH("pass_len", new String[0]),  CONFIG_RELOAD_SUCCESS("reload", new String[0]),  LOGIN_TIMEOUT_ERROR("timeout", new String[0]),  USAGE_CHANGE_PASSWORD("usage_changepassword", new String[0]),  INVALID_NAME_LENGTH("name_len", new String[0]),  INVALID_NAME_CHARACTERS("regex", new String[] { "REG_EX" }),  ADD_EMAIL_MESSAGE("add_email", new String[0]),  FORGOT_PASSWORD_MESSAGE("recovery_email", new String[0]),  USAGE_CAPTCHA("usage_captcha", new String[] { "<theCaptcha>" }),  CAPTCHA_WRONG_ERROR("wrong_captcha", new String[] { "THE_CAPTCHA" }),  CAPTCHA_SUCCESS("valid_captcha", new String[0]),  KICK_FOR_VIP("kick_forvip", new String[0]),  KICK_FULL_SERVER("kick_fullserver", new String[0]),  USAGE_ADD_EMAIL("usage_email_add", new String[0]),  USAGE_CHANGE_EMAIL("usage_email_change", new String[0]),  USAGE_RECOVER_EMAIL("usage_email_recovery", new String[0]),  INVALID_NEW_EMAIL("new_email_invalid", new String[0]),  INVALID_OLD_EMAIL("old_email_invalid", new String[0]),  INVALID_EMAIL("email_invalid", new String[0]),  EMAIL_ADDED_SUCCESS("email_added", new String[0]),  CONFIRM_EMAIL_MESSAGE("email_confirm", new String[0]),  EMAIL_CHANGED_SUCCESS("email_changed", new String[0]),  EMAIL_SHOW("email_show", new String[] { "%email" }),  SHOW_NO_EMAIL("show_no_email", new String[0]),  RECOVERY_EMAIL_SENT_MESSAGE("email_send", new String[0]),  COUNTRY_BANNED_ERROR("country_banned", new String[0]),  ANTIBOT_AUTO_ENABLED_MESSAGE("antibot_auto_enabled", new String[0]),  ANTIBOT_AUTO_DISABLED_MESSAGE("antibot_auto_disabled", new String[] { "%m" }),  EMAIL_ALREADY_USED_ERROR("email_already_used", new String[0]),  TWO_FACTOR_CREATE("two_factor_create", new String[] { "%code", "%url" }),  NOT_OWNER_ERROR("not_owner_error", new String[0]),  INVALID_NAME_CASE("invalid_name_case", new String[] { "%valid", "%invalid" }),  TEMPBAN_MAX_LOGINS("tempban_max_logins", new String[0]),  ACCOUNTS_OWNED_SELF("accounts_owned_self", new String[] { "%count" }),  ACCOUNTS_OWNED_OTHER("accounts_owned_other", new String[] { "%name", "%count" }),  KICK_FOR_ADMIN_REGISTER("kicked_admin_registered", new String[0]),  INCOMPLETE_EMAIL_SETTINGS("incomplete_email_settings", new String[0]),  EMAIL_SEND_FAILURE("email_send_failure", new String[0]),  RECOVERY_CODE_SENT("recovery_code_sent", new String[0]),  INCORRECT_RECOVERY_CODE("recovery_code_incorrect", new String[] { "%count" }),  RECOVERY_TRIES_EXCEEDED("recovery_tries_exceeded", new String[0]),  RECOVERY_CODE_CORRECT("recovery_code_correct", new String[0]),  RECOVERY_CHANGE_PASSWORD("recovery_change_password", new String[0]),  CHANGE_PASSWORD_EXPIRED("change_password_expired", new String[0]),  EMAIL_COOLDOWN_ERROR("email_cooldown_error", new String[] { "%time" }),  VERIFICATION_CODE_REQUIRED("verification_code_required", new String[0]),  USAGE_VERIFICATION_CODE("usage_verification_code", new String[0]),  INCORRECT_VERIFICATION_CODE("incorrect_verification_code", new String[0]),  VERIFICATION_CODE_VERIFIED("verification_code_verified", new String[0]),  VERIFICATION_CODE_ALREADY_VERIFIED("verification_code_already_verified", new String[0]),  VERIFICATION_CODE_EXPIRED("verification_code_expired", new String[0]),  VERIFICATION_CODE_EMAIL_NEEDED("verification_code_email_needed", new String[0]),  SECOND("second", new String[0]),  SECONDS("seconds", new String[0]),  MINUTE("minute", new String[0]),  MINUTES("minutes", new String[0]),  HOUR("hour", new String[0]),  HOURS("hours", new String[0]),  DAY("day", new String[0]),  DAYS("days", new String[0]);
  
  private String key;
  private String[] tags;
  
  private MessageKey(String key, String... tags)
  {
    this.key = key;
    this.tags = tags;
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public String[] getTags()
  {
    return this.tags;
  }
  
  public String toString()
  {
    return this.key;
  }
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\message\MessageKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */