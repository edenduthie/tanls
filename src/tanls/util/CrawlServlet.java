package tanls.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public final class CrawlServlet implements Filter {
	
	  public static final String HEADLESS_URL = "http://ec2-54-252-6-134.ap-southeast-2.compute.amazonaws.com:1337/?url=";
	
	  public static final Logger log = Logger.getLogger(CrawlServlet.class);

	  public void doFilter(ServletRequest request, ServletResponse response,
	      FilterChain chain) throws IOException {
		  
		  response.setContentType("text/html");
		  
		  String url_with_escaped_fragment = ((HttpServletRequest)request).getRequestURL().toString();
		  String queryString = ((HttpServletRequest)request).getQueryString();

	      if ((queryString != null) && (queryString.contains("_escaped_fragment_"))) {

	       // rewrite the URL back to the original #! version
	       // remember to unescape any %XX characters
	       String url_with_hash_fragment = rewriteQueryString(url_with_escaped_fragment+"?"+queryString);
	       
	       String bromBoneUrl = HEADLESS_URL + URLEncoder.encode(url_with_hash_fragment, "UTF-8");
	       
	       bromBoneUrl = bromBoneUrl.replace("%21","!");
	       
	       URL url = new URL(bromBoneUrl);
	       URLConnection connection = url.openConnection();
	       InputStream input = connection.getInputStream();
	       OutputStream output = response.getOutputStream();
	       
	       byte[] buffer = new byte[1024]; // Adjust if you want
	       int bytesRead;
	       while ((bytesRead = input.read(buffer)) != -1)
	       {
	           output.write(buffer, 0, bytesRead);
	       }
           input.close();
           output.close();
	       
	     } else {
	      try {
	        // not an _escaped_fragment_ URL, so move up the chain of servlet (filters)
	        chain.doFilter(request, response);
	      } catch (ServletException e) {
	        log.error("Servlet exception caught: " + e);
	        e.printStackTrace();
	      }
	    
	  }
	}
	  
	  public String rewriteQueryString(String url_with_escaped_fragment)
	  {
		  return url_with_escaped_fragment.replace("?_escaped_fragment_=","#!");
	  }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}