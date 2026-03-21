package frc.robot.SubSystem.Logging;

import java.util.HashMap;
import java.util.function.BooleanSupplier;
import java.util.jar.Attributes.Name;

import edu.wpi.first.networktables.BooleanArrayEntry;
import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StructArrayEntry;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructEntry;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.util.struct.Struct;
import edu.wpi.first.util.struct.StructSerializable;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class NerdLog {

    static NetworkTable baseTable;
    static HashMap<String, StructPublisher> structPublishers; 
    static HashMap<String, DoubleEntry> doubleEntries;
    static HashMap<String, DoubleArrayEntry> doubleArrayEntries;
    static HashMap<String, StructArrayPublisher> structArrayPublishers;
    static HashMap<String, BooleanEntry> booleanEntries;
    static HashMap<String, BooleanArrayEntry> boolArrayEntries;
    static NetworkTableInstance tableInst;


    /**
     * starts the logging and fetches es all the annotations
     */
    public static void startLog() {
        startLog("", "");
    }

    /**
     * starts the logging and fetches all the annotations
     */
    public static void startLog(String directory) {
        startLog(directory,"");
    }
    
    /**
     * starts the logging and fetches all the annotations
     */
    public static void startLog(String directory, String fileName) {
        DataLogManager.start(directory, fileName);
        DriverStation.startDataLog(DataLogManager.getLog());

        tableInst = NetworkTableInstance.getDefault();
        baseTable = tableInst.getTable("Robot");
        structPublishers = new HashMap<>();
        doubleEntries = new HashMap<>();
        doubleArrayEntries = new HashMap<>();
        structArrayPublishers = new HashMap<>();
        booleanEntries = new HashMap<>();
        boolArrayEntries = new HashMap<>();
        
    }

    //uses reflection and reflection scares me...
    public static void logClassesWithAnnotation(Class... classes) {

    }


    public static <T extends StructSerializable> void logStructvariable(String name, T variable, Struct variableStruct) {
        // if the variable doesn't already exist, add it.
        if (/*!structVariables.containsKey(name) &&*/ !structPublishers.containsKey(name)) {
            StructPublisher<T> structPub = baseTable.getStructTopic(name, variableStruct).publish();
            structPublishers.put(name, structPub);
        }

        StructPublisher<T> structPub = structPublishers.get(name);
        structPub.set(variable);
    }

    public static <T extends StructSerializable> void LogStructArray(String name, T[] variables, Struct variableStruct) {
        if (!structArrayPublishers.containsKey(name)) {
            StructArrayPublisher<T> structPub = baseTable.getStructArrayTopic(name, variableStruct).publish();
            structArrayPublishers.put(name, structPub);
        }
        StructArrayPublisher structPub = structArrayPublishers.get(name);
        structPub.set(variables);
    }

    public static void logDouble(String name, double variable) {
        if (!doubleEntries.containsKey(name)) {
            DoubleEntry dubPub = baseTable.getDoubleTopic(name).getEntry(variable);
            doubleEntries.put(name, dubPub);
        }

        DoubleEntry dubPub = doubleEntries.get(name);
        dubPub.set(variable);
    }

    /**
     * make sure the variable has at least been given a initial logvar() before using this method
     * @param name - the name of the variable you are getting
     * @return - the value as presented on a dashboard
     */
    public static double getdouble(String name) {
        if (!doubleEntries.containsKey(name)) return 0.0;

        return doubleEntries.get(name).get();
    }

    public static void logDoubleArray(String name, double[] variable) {
        if (!doubleArrayEntries.containsKey(name)) {
            DoubleArrayEntry dubPub = baseTable.getDoubleArrayTopic(name).getEntry(variable);
            doubleArrayEntries.put(name, dubPub);
        }

        DoubleArrayEntry dubPub = doubleArrayEntries.get(name);
        dubPub.set(variable);
    }

    public static double[] getDoubleArray(String name) {
        if (!doubleArrayEntries.containsKey(name)) return new double[0];

        return doubleArrayEntries.get(name).get();
    }

    public static void logBooleanVariable(String name, boolean variable) {
        if (!booleanEntries.containsKey(name)) {
            BooleanEntry boolPub = baseTable.getBooleanTopic(name).getEntry(variable);
            booleanEntries.put(name, boolPub);
        }

        BooleanEntry boolPub = booleanEntries.get(name);
        boolPub.set(variable);
    }

    public static Boolean getBoolean(String name) {
        if (!booleanEntries.containsKey(name)) return false;

        return booleanEntries.get(name).get();
    }

    public static void logBooleanArray(String name, boolean[] variables) {
        if (!boolArrayEntries.containsKey(name)) {
            BooleanArrayEntry boolArrPub= baseTable.getBooleanArrayTopic(name).getEntry(variables);
            boolArrayEntries.put(name, boolArrPub);
        }

        BooleanArrayEntry boolArrPub = boolArrayEntries.get(name);
        boolArrPub.set(variables);
    }

    public static boolean[] getBooleanArray(String name) {
        if (!boolArrayEntries.containsKey(name)) return new boolean[0];

        return boolArrayEntries.get(name).get();
    }

    }
