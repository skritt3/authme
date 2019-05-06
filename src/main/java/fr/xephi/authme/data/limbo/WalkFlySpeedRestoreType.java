//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.data.limbo;

import org.bukkit.entity.Player;

public enum WalkFlySpeedRestoreType {
  RESTORE {
    public void restoreFlySpeed(Player player, LimboPlayer limbo) {
      player.setFlySpeed(limbo.getFlySpeed());
    }

    public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
      player.setWalkSpeed(limbo.getWalkSpeed());
    }
  },
  RESTORE_NO_ZERO {
    public void restoreFlySpeed(Player player, LimboPlayer limbo) {
      float limboFlySpeed = limbo.getFlySpeed();
      player.setFlySpeed(limboFlySpeed > 0.01F ? limboFlySpeed : 0.1F);
    }

    public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
      float limboWalkSpeed = limbo.getWalkSpeed();
      player.setWalkSpeed(limboWalkSpeed > 0.01F ? limboWalkSpeed : 0.2F);
    }
  },
  MAX_RESTORE {
    public void restoreFlySpeed(Player player, LimboPlayer limbo) {
      player.setFlySpeed(Math.max(player.getFlySpeed(), limbo.getFlySpeed()));
    }

    public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
      player.setWalkSpeed(Math.max(player.getWalkSpeed(), limbo.getWalkSpeed()));
    }
  },
  DEFAULT {
    public void restoreFlySpeed(Player player, LimboPlayer limbo) {
      player.setFlySpeed(0.1F);
    }

    public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
      player.setWalkSpeed(0.2F);
    }
  };

  private WalkFlySpeedRestoreType() {
  }

  public abstract void restoreFlySpeed(Player var1, LimboPlayer var2);

  public abstract void restoreWalkSpeed(Player var1, LimboPlayer var2);
}
