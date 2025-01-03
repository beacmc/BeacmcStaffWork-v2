package com.beacmc.beacmcstaffwork.api.menu;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClickMethod {
   private final Method method;
   private final List<Integer> slots;

   public ClickMethod(Method method, int[] slots) {
      this.method = method;
      this.slots = Arrays.stream(slots).boxed().collect(Collectors.toList());
   }

   public Method getMethod() {
      return this.method;
   }

   public List<Integer> getSlots() {
      return this.slots;
   }
}
