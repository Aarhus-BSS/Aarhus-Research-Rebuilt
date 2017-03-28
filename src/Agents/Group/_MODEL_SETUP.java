/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

/**
 *
 * @author d3vil401
 */
public enum _MODEL_SETUP 
{
    MODEL_1A,
    MODEL_1B,
    MODEL_1A_WR,
    MODEL_1B_WR;
    
    private _MODEL_SETUP() 
    {
        
    }
    
    public static _MODEL_SETUP fromString(String _from)
    {
        switch (_from.toUpperCase()) 
        {
            case "1A":
                return MODEL_1A;
            case "1B":
                return MODEL_1B;
            case "1A_WR":
                return MODEL_1A_WR;
            case "1B_WR":
                return MODEL_1B_WR;
                
            default:
                return MODEL_1A;
        }
    }
}
