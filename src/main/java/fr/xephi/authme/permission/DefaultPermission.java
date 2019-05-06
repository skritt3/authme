//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.xephi.authme.permission;

import org.bukkit.permissions.ServerOperator;

public enum DefaultPermission {
  NOT_ALLOWED {
    public boolean evaluate(ServerOperator sender) {
      return false;
    }
  },
  OP_ONLY {
    public boolean evaluate(ServerOperator sender) {
      return sender != null && sender.isOp();
    }
  },
  ALLOWED {
    public boolean evaluate(ServerOperator sender) {
      return true;
    }
  };

  private DefaultPermission() {
  }

  public abstract boolean evaluate(ServerOperator var1);
}
