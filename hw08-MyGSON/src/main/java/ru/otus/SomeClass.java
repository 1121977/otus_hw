package ru.otus;

import java.util.Arrays;
import java.util.List;

public class SomeClass {
    private int a;
    private List<?> b;
    private float[] c;
    private String d;
    private double e;
    private Short f;

    public void setA(int a) {
        this.a = a;
    }

    public void setB(List<?> b) {
        this.b = b;
    }

    public void setC(float[] c) {
        this.c = c;
    }

    public void setD(String d) {
        this.d = d;
    }

    public void setE(double e) {
        this.e = e;
    }

    public void setF(Short f) {
        this.f = f;
    }

    private SomeClass(int a, List<?> b, float[] c, String d, double e, Short f) {
        this.a = a;
        this.b = b.subList(0, b.size());
        this.c = c.clone();
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public static class Builder {
        private int a;
        private List<?> b;
        private float[] c;
        private String d;
        private double e;
        private Short f;

        SomeClass build() {
            return new SomeClass(this.a, this.b, this.c, this.d, this.e, this.f);
        }

        public Builder setA(int a) {
            this.a = a;
            return this;
        }

        public Builder setB(List<?> b) {
            this.b = b;
            return this;
        }

        public Builder setC(float[] c) {
            this.c = c;
            return this;
        }

        public Builder setD(String d) {
            this.d = d;
            return this;
        }

        public Builder setE(double e) {
            this.e = e;
            return this;
        }

        public Builder setF(Short f) {
            this.f = f;
            return this;
        }
    }

    public int getA() {
        return a;
    }

    public List<?> getB(){
        return b.subList(0,b.size());
    }

    public float[] getC(){
        return c.clone();
    }

    public String getD(){
        return d;
    }

    public double getE(){
        return e;
    }

    public Short getF(){
        return Short.valueOf(f);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = true;
        if (this == obj){
            return result;
        }
        if(this.getClass().equals(obj.getClass())){
            SomeClass someObject = (SomeClass) obj;
            if (this.a==someObject.getA()){
                if (this.b.equals(someObject.getB())){
                    if (Arrays.equals(this.c,someObject.getC())){
                        if (this.d.equals(someObject.getD())){
                            if(this.e == someObject.getE()){
                                if (this.f.equals(someObject.getF())){

                                } else {
                                    result = false;
                                }
                            } else {
                                result = false;
                            }
                        } else {
                            result = false;
                        }
                    } else {
                        result = false;
                    }
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }
}
