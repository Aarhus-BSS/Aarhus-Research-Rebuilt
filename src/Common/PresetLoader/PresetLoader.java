/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.PresetLoader;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import auresearch.Globals;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author d3vil401
 */
public class PresetLoader 
{
    private static final Map<String, ConfigManager> _loaderList = new HashMap<>();
    private static ConfigManager _originalMain = null;
   
    public static int getListSize()
    {
        return _loaderList.size();
    }
    
    public static void assignOriginal(ConfigManager _original)
    {
        _originalMain = _original;
    }
    
    public static void revert()
    {
        FactoryHolder._configManager = _originalMain;
    }
    
    public static String[] getNameList()
    {
        String[] _names = new String[_loaderList.size()];
        int i = 0;
        
        for (Map.Entry _entry: _loaderList.entrySet())
            _names[i++] = String.valueOf(_entry.getKey());
        
        return _names;
    }
    
    public static ConfigManager getConfigurationByName(String _name)
    {
        return _loaderList.get(_name);
    }
    
    public static boolean loadList()
    {
        File _dir = new File(FactoryHolder._configManager.getStringValue("PRESET_INPUT_FOLDER"));
        
        File[] _files = _dir.listFiles((d, name) -> name.endsWith(".cfg"));
        
        for (int i = 0; i < _files.length; i++)
        {
            try {
                ConfigManager _slave = new ConfigManager(_files[i]);
                
                if (_slave.getNumberValue("SCENARIO_VERSION") != Globals._softwareVersion)
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_WARNING, "This configuration sceario is using a different version, some incompatibility issues may rise.");
                
                if (!_loaderList.containsKey(_slave.getStringValue("SCENARIO_NAME")))
                    _loaderList.put(_slave.getStringValue("SCENARIO_NAME"), _slave);
                else
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Could not load " + _files[i].getName() + ": scenario name already exists, skipping.");
                
                
                //FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Loaded " + _files[i].getName() + " preset (" + (i + 1) + " / " + _files.length + ").");
            } catch (IOException ex) {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Cannot read " + _files[i].getName() + ": " + ex.getMessage());
                return false;
            }
        }
        
        return true;
    }
}
