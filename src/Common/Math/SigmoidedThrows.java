/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Math;

import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.Random;

/**
 *
 * @author d3vil401
 */
public class SigmoidedThrows 
{
    private static Random _random = new Random();
    
    private static double sigmoid(double x) 
    {
        return (1 / (1 + Math.pow(Math.E, (-1 * x))));
    }
    
    public static double[] getSigmoidMap(int[] _chSkillMap, int[] _agentSkillMap)
    {
        double[] _sigMap = null;
        if (_chSkillMap.length != _agentSkillMap.length)
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Skill maps sizes are different in sigmoidation.");
        else {
            _sigMap = new double[_chSkillMap.length];
            for (int i = 0; i < _sigMap.length; i++)
               _sigMap[i] = (sigmoid((double)(_agentSkillMap[i] - _chSkillMap[i]) / 10));
        }
        
        return _sigMap;
    }
    
    public static boolean throwOnSigmoid(double _requirement)
    {
        double _throw = 0;
        int _throwScore = 0;
        
        _throw = _random.nextDouble();
        if (_throw <= _requirement)
            return true;
        
        return false;
    }
    
    public static boolean throwOnSigmoid(double[] _requirements)
    {
        double _throw = 0;
        int _throwScore = 0;
        for (int i = 0; i < _requirements.length; i++)
        {
            _throw = _random.nextDouble();
            if (_throw <= _requirements[i])
                _throwScore++;
        }
        
        if (_throwScore == _requirements.length)
            return true;
        
        return false;
    }
}
