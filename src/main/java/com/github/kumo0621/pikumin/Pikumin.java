package com.github.kumo0621.pikumin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Pikumin extends JavaPlugin implements org.bukkit.event.Listener{

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.broadcastMessage("aaaaaa");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    Map<Team, Entity> map = new HashMap<>();
    Collection<? extends Player> player = Bukkit.getOnlinePlayers();
    @EventHandler
    public void onEntityTargetEvent (final EntityTargetEvent ete) {
        for (Player onlinePlayer : player) {
            Team team = onlinePlayer.getScoreboard().getEntryTeam(onlinePlayer.getName());
            if (team != null) {
                Entity entity = map.get(team);
                ete.setTarget(entity);
            }
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("pikumin")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage("引数を指定してください。");
                } else {
                    switch (args[0]) {
                        case "test":
                            sender.sendMessage("テスト");
                            break;
                        case "summon":
                            map.clear();
                            for (Player onlinePlayer : player) {
                                Location location = onlinePlayer.getLocation();
                                Team team = onlinePlayer.getScoreboard().getEntryTeam(onlinePlayer.getName());
                                if (team != null) {
                                    if (!map.containsKey(team)) {
                                        @NotNull Entity entity = location.getWorld().spawn(location, Zombie.class);
                                        entity.addScoreboardTag(team.getName());
                                        map.put(team, entity);
                                        sender.sendMessage("アーマースタンドを召喚しました。");
                                    }
                                } else {
                                    sender.sendMessage("チームが存在しません");
                                }
                            }
                            break;
                        default:
                            sender.sendMessage("不明なコマンドです。");
                            break;
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }
}
