package com.example.kuba.repulsev001;

import java.util.*;


/**
 * A class for solving polynomials
 */
public class Polynomial{

    public ArrayList<Float> Coefficient;

    public Polynomial()
    {
        Coefficient = new ArrayList<Float>();
    }

    public Polynomial(Float... coef)
    {
        Coefficient = new ArrayList<Float>(Arrays.asList(coef));
    }

    public int deg()
    {
        return Coefficient.size()-1;
    }

    public float value(Float x)
    {
        float val=0;
        float x_pow=1;
        for(int i = 0; i< Coefficient.size(); i++)
        {
            val+=(x_pow* Coefficient.get(i));
            x_pow*=x;
        }
        return val;
    }

    public void AddConst(float a)
    {
        for(int i = 0; i< Coefficient.size(); i++)
        {
            Coefficient.add(i, Coefficient.get(i)+a);
        }
        while(Coefficient.size()>=1 && Coefficient.get(Coefficient.size()-1)==0)
        {
            Coefficient.remove(Coefficient.size()-1);
        }
    }

    public void MultiplyByConst(float a)
    {
        for(int i = 0; i< Coefficient.size(); i++)
        {
            Coefficient.add(i, Coefficient.get(i)*a);
        }
        while(Coefficient.size()>=1 && Coefficient.get(Coefficient.size()-1)==0)
        {
            Coefficient.remove(Coefficient.size()-1);
        }
    }

    public void Add(Polynomial P)
    {
        while(Coefficient.size()<P.Coefficient.size())
        {
            Coefficient.add(0.f);
        }
        for(int i = 0; i<Math.min(Coefficient.size(),P.Coefficient.size()); i++)
        {
            Coefficient.add(i, Coefficient.get(i)+P.Coefficient.get(i));
        }
        while(Coefficient.size()>=1 && Coefficient.get(Coefficient.size()-1)==0)
        {
            Coefficient.remove(Coefficient.size()-1);
        }
    }
    public void Subtract(Polynomial P)
    {
        while(Coefficient.size()<P.Coefficient.size())
        {
            Coefficient.add(0.f);
        }
        for(int i = 0; i<Math.min(Coefficient.size(),P.Coefficient.size()); i++)
        {
            Coefficient.add(i, Coefficient.get(i)-P.Coefficient.get(i));
        }
        while(Coefficient.size()>=1 && Coefficient.get(Coefficient.size()-1)==0)
        {
            Coefficient.remove(Coefficient.size()-1);
        }
    }
    public float firstRootFrom(float adr,float end,float aprox,float dx) //first root in <adr,end> ; if none return NaN
    {
        while(Math.abs(value(adr))>aprox && adr<=end)
        {
            adr+=dx;
        }
        if(Math.abs(value(adr))<=aprox)
            return adr;
        return 1.f/0.f;
    }
    public float firstRootFrom(float adr,float end)
    {
        return firstRootFrom(adr,end, 0.01f,0.01f);
    }

};
