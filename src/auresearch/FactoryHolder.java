package auresearch;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Graphics.GraphicManager.GraphicManager;
import PictureManipulation.PictureMerger;
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
    // Graphics generator engine
    public static GraphicManager    _graphicManager = new GraphicManager();
    // Configuration engine
    public static ConfigManager     _configManager = null;
    
    // Documentation (XLS & Graphics) are going to follow these names, pay attention to the flags {}
    // Malte EDIT: Added the random tag to make sure it doesnt overwrite itself for multiple runs
    public static String[] _docNames = {"ProblemsReport.{DATETIME}-{HAS_GROUPS}.{RANDOM}", 
        "AgentReport.{DATETIME}-{HAS_GROUPS}.{RANDOM}", "AVGReport.{DATETIME}-{HAS_GROUPS}.{RANDOM}", 
        "DetailReport.{DATETIME}-{HAS_GROUPS}.{RANDOM}", "CompositeReport.{DATETIME}-{HAS_GROUPS}.{RANDOM}",
        "GroupReport.{DATETIME}-{HAS_GROUPS}.{RANDOM}"};
    
    public static String[] _graphNames = {"AVGAgentsPerRound.{DATETIME}-{HAS_GROUPS}.{RANDOM}.png", 
        "AgentsPerRound.{DATETIME}-{HAS_GROUPS}.{RANDOM}.png", "SAgentsSolvedPerRound.{DATETIME}-{HAS_GROUPS}.{RANDOM}.png", 
        "CompositeEXP.{DATETIME}-{HAS_GROUPS}.{RANDOM}.png", "CompositeProblems.{DATETIME}-{HAS_GROUPS}.{RANDOM}.png" };
    
    public static PictureMerger _agentsCount    = new PictureMerger();
    public static PictureMerger _compositeChart = new PictureMerger();
    
    public static ChartPanel[] _graphsRender = null;
    
    public static Globals _GLOBALS;
    
    public static void destroy()
    {
        //_logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Destroying everything.");
        //_logManager.destroySession();
    }
}
