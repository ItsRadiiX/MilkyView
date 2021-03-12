package com.itsradiix.milkyview.models.data;

import com.itsradiix.milkyview.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class Utils {

	public static ItemStack makeItem(Material material, String displayName, String... lore) {
		ItemStack item = new ItemStack(material);
		return getItemStack(item, displayName, lore);
	}

	public static ItemStack makeItem(ItemStack itemStack, String displayName, String... lore) {
		return getItemStack(itemStack, displayName, lore);
	}

	@NotNull
	private static ItemStack getItemStack(ItemStack itemStack, String displayName, String[] lore) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(Main.chatColor(displayName));
		List<String> tmpLore = Arrays.asList(lore);

		for (int i = 0; i < tmpLore.size(); i++) {
			tmpLore.set(i, Main.chatColor(tmpLore.get(i)));
		}

		itemMeta.setLore(tmpLore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
}
