package auresearch;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Common.Lua.LuaManager;
import Graphics.GraphicManager.GraphicManager;
import org.jfree.chart.ChartPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author d3vil401
 */
public class FactoryHolder 
{
    /*
    
        This class is supposed to hold global variables and classes.
    
    */
    // Logs
    public static ILogManager       _logManager = null;
    // Scripting Engine
    public static LuaManager        _luaManager = null;
    // Graphics generator engine
    public static GraphicManager    _graphicManager = new GraphicManager();
    // Configuration engine
    public static ConfigManager     _configManager = null;
    
    // Documentation (XLS & Graphics) are going to follow these names, pay attention to the flags {}
    public static String[] _docNames = {"ProblemsReport.{DATETIME}-{HAS_GROUPS}", 
        "AgentReport.{DATETIME}-{HAS_GROUPS}", "AVGReport.{DATETIME}-{HAS_GROUPS}", 
        "DetailReport.{DATETIME}-{HAS_GROUPS}", "CompositeReport.{DATETIME}-{HAS_GROUPS}",
        "GroupReport.{DATETIME}-{HAS_GROUPS}"};
    
    public static String[] _graphNames = {"AVGAgentsPerRound.{DATETIME}-{HAS_GROUPS}.png", 
        "AgentsPerRound.{DATETIME}-{HAS_GROUPS}.png", "SAgentsSolvedPerRound.{DATETIME}-{HAS_GROUPS}.png", 
        "CompositeEXP.{DATETIME}-{HAS_GROUPS}.png", "CompositeProblems.{DATETIME}-{HAS_GROUPS}.png" };
    
    public static ChartPanel[] _graphsRender = null;
    
    public static Globals _GLOBALS;
    
    public static void destroy()
    {
        _logManager.destroySession();
    }
}
