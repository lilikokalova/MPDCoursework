// Name: Liliya Kokalova Matric number: S1630528
package com.example.coursework;

public class Roadworks {
    private String title;
    private String description;


    public Roadworks()
    {
        title = "";
        description = "";
    }

    public Roadworks(String atitle,String adescription)
    {
        title = atitle;
        description = adescription;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String atitle)
    {
        title = atitle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String adescription)
    {
        description = adescription;
    }

    //public String toString()
   // {
       // String temp;

       // temp = bolt + " " + washer + " " + nut;

       // return temp;
   // }
}
