package ru.vsu.cs.dolzhenko_m_s.graphicdrawer.domain;

public class Result {
    public double acc;
    public String rest;

    public Result(double v, String r)
    {
        this.acc = v;
        this.rest = r;
    }
}
