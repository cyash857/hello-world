package com.example;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;

public class HelloWorldExample extends HttpServlet 
{
  public void service( HttpServletRequest req, HttpServletResponse res ) throws IOException {
    PrintWriter out = res.getWriter();
    out.println( "Hello, World!" );
    out.close();
  }
}
