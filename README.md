# BeacmcStaffWork

> [!IMPORTANT]
> The plugin requires Java 16 and server version 1.16
> 
# Developers
create a new `Action`

```java
public class ExampleAction extends Action {

    @Override
    public String getName() {
        return "[example_action]";
    }

    @Override
    public String getDescription() {
        return "description of this action";
    }

    @Override
    public void execute(StaffPlayer staffPlayer, String param) {
        // your code
    }
}
```
register a new `Action`
```java
public class ExamplePlugin extends JavaPlugin {

    private static ActionManager actionManager;

    @Override
    public void onEnable() {
        actionManager = BeacmcStaffWork.getActionManager();
        actionManager.registerAction(new ExampleAction());
    }
}
```
