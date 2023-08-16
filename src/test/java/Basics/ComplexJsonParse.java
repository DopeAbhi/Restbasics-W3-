package Basics;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
    public static void main(String[] args) {

        System.out.println("First Test Case");
        JsonPath js = new JsonPath(payload.CoursePrice());
        int count = js.getInt("courses.size()");
        System.out.println(count);


        System.out.println("Second Test Cases");
        int totalamount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalamount);

        System.out.println("Third Test Cases");
        String titlefirstcourse = js.get("courses[0].title");
        System.out.println(titlefirstcourse);

        //Print all the courses and their respective price

        System.out.println("Fourth Test Cases");
        for (int i = 0; i < count; i++)
        {
            System.out.println(js.get("courses["+i+"].title").toString());
            System.out.println(js.get("courses["+i+"].price").toString());
        }
//Print number of copies sold by any course
        System.out.println("Fifth Test Cases");
        for (int i = 0; i < count; i++)
        {
           String coursetitle= js.get("courses["+i+"].title");
           if (coursetitle.equalsIgnoreCase("RPA"))
           {
               int copiessold=js.get("courses["+i+"].copies");
               System.out.println(copiessold);
               break;
           }

        }
        //Check purchase amount equal to the number of copies sold
        System.out.println("sixth Test Case");
int sum=0;
        for (int i = 0; i < count; i++)
        {
          sum=sum+ js.getInt("courses["+i+"].price")* js.getInt("courses["+i+"].copies");

        }
        if(sum==totalamount)
        {
            System.out.println(sum);

        }

    }
}
