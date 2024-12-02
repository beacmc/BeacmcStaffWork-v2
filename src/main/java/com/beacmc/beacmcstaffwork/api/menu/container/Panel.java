package com.beacmc.beacmcstaffwork.api.menu.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.beacmc.beacmcstaffwork.api.menu.annotation.Click;
import com.beacmc.beacmcstaffwork.api.menu.annotation.Close;
import com.beacmc.beacmcstaffwork.api.menu.annotation.Drag;
import com.beacmc.beacmcstaffwork.api.menu.annotation.Open;
import com.beacmc.beacmcstaffwork.api.menu.listener.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Panel implements InventoryHolder {

   protected Player player;
   protected Inventory inventory;
   private final Set<Method> close;
   private final Set<Method> drag;
   private final Set<Method> open;
   private final Set<ClickMethod> click;

   public Panel(JavaPlugin plugin) {
      MenuListener.register(plugin);
      this.close = new HashSet<>();
      this.click = new HashSet<>();
      this.drag = new HashSet<>();
      this.open = new HashSet<>();
      Method[] methods = this.getClass().getMethods();
      int methodsSize = methods.length;

      for(int i = 0; i < methodsSize; ++i) {
         Method method = methods[i];
         if (method.isAnnotationPresent(Click.class)) {
            this.click.add(new ClickMethod(method, (method.getDeclaredAnnotation(Click.class)).slots()));
         } else if (method.isAnnotationPresent(Close.class)) {
            this.close.add(method);
         } else if (method.isAnnotationPresent(Drag.class)) {
            this.drag.add(method);
         } else if (method.isAnnotationPresent(Open.class)) {
            this.open.add(method);
         }
      }
   }

   public void open(Player player) {
      this.player = player;
      this.inventory = Bukkit.createInventory(this, this.getSize(), this.getTitle());
      this.setItems();
      player.openInventory(this.inventory);
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public final void handleClick(InventoryClickEvent event) {
      this.click.forEach((m) -> {
         try {
            if (m.getSlots().isEmpty() || m.getSlots().contains(event.getSlot())) {
               event.setCancelled(true);
               m.getMethod().invoke(this, event);
            }
         } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
         }
      });
   }

   public final void handleClose(InventoryCloseEvent event) {
      this.close.forEach((m) -> {
         try {
            m.invoke(this, event);
         } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
         }
      });
   }

   public final void handleDrag(InventoryDragEvent event) {
      this.drag.forEach((m) -> {
         try {
            m.invoke(this, event);
         } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
         }
      });
   }

   public final void handleOpen(InventoryOpenEvent event) {
      this.open.forEach((m) -> {
         try {
            m.invoke(this, event);
         } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
         }
      });
   }

   public void update() {
      setItems();
      player.updateInventory();
   }

   public abstract int getSize();

   public abstract String getTitle();

   public abstract void setItems();
}