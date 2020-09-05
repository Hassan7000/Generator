package me.hassan.riftgenerators.safenbt;

import java.lang.reflect.Method;
import java.util.AbstractList;

import org.bukkit.Bukkit;

import io.netty.util.internal.UnstableApi;
import me.hassan.riftgenerators.RiftGenerators;

public class SafeNBTList{

    private static final String version = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String cbVersion = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Class<?> tagListClass;
    private static Class<?> nbtBaseClass;

    private final Object tagList;

    static{
        try{
            tagListClass = Class.forName(version + ".NBTTagList");
            nbtBaseClass = Class.forName(version + ".NBTBase");
        }
        catch(Exception ex){
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(RiftGenerators.getInstance());
        }
    }

    @UnstableApi
    public Object getTagList(){
        return tagList;
    }

    public SafeNBTList(){
        this(null);
    }

    public SafeNBTList(Object tagCompound){
        Object toSet = tagCompound;
        if(tagCompound == null){
            try{
                toSet = tagListClass.newInstance();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        this.tagList = toSet;
    }

    public boolean isEmpty(){
        try{
            Method m = tagListClass.getMethod("isEmpty");
            m.setAccessible(true);
            Object r = m.invoke(this.tagList);
            m.setAccessible(false);
            return r instanceof Boolean ? (Boolean) r : true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return true;
        }
    }

    public void add(SafeNBT value){
        add(value.getTagCompund());
    }

    public <T> void add(SafeNBTBaseType type, T value){
        add(type.make(value));
    }

    public <T> void add(SafeNBTBaseType type, T... values){
        for(T value : values){
            add(type, value);
        }
    }

    public <T> void addGeneric(T value){
        if(value == null){return;}
        SafeNBTBaseType type = SafeNBTBaseType.get(value.getClass());
        if(type == null){return;}
        add(type, value);
    }

    public <T> void add(T... values){
        SafeNBTBaseType type = values.length > 0 ? SafeNBTBaseType.getByClass(values[0].getClass()) : null;
        if(type != null){
            add(type, values);
        }
    }

    private void add(Object nbt){
        try{
            Method m = AbstractList.class.getMethod("add", Object.class);
            m.setAccessible(true);
            m.invoke(tagList, nbt);
            m.setAccessible(false);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

	
}

