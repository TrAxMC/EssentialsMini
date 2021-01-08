package de.framedev.essentialsmin.managers;


/*
 * de.framedev.essentialsmin.managers
 * ===================================================
 * This File was Created by FrameDev
 * Please do not change anything without my consent!
 * ===================================================
 * This Class was created at 28.09.2020 10:12
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.framedev.essentialsmin.main.Main;
import de.framedev.essentialsmin.utils.CustomJson;
import de.framedev.essentialsmin.utils.JsonHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Locations {


    private static class Utils {
        public String locationToString(Location location) {
            String s = "";
            s += location.getWorld().getName() + ";";
            s += location.getX() + ";";
            s += location.getY() + ";";
            s += location.getZ() + ";";
            s += location.getYaw() + ";";
            s += location.getPitch();
            /* World;10;63;12;160;-24 */
            return s;
        }

        public Location locationFromString(String string) {
            String[] s = string.split(";");
            return new Location(Bukkit.getWorld(s[0]), Double.parseDouble(s[1]), Double.parseDouble(s[2]), Double.parseDouble(s[3]), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
        }
    }

    private String name;
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private final File file = new File(Main.getInstance().getDataFolder(),"Locations.json");

    CustomJson json = new CustomJson(file);

    public Locations(String name) {
        this.name = name;
    }

    public Locations(String name, Location location) {
        this.name = name;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = location.getWorld().getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setLocation() {
        json.set(name, new Utils().locationToString(new Location(Bukkit.getWorld(world),x,y,z,yaw,pitch)));
        json.saveConfig();
    }

    public Location getLocation() {
        if(json.contains(name)) {
            return new Utils().locationFromString(json.getString(name));
        }
        return null;
    }
}
