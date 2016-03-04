/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.searcher;

import se.constant.Constants;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author giacatho
 */
public class SampleSearchServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String q = "Complementation";

            List<String> results = search(Constants.INDEX_DIR, q);
        
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Searcher</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Searcher at " + request.getContextPath() + "</h1>");
            
            if (results.isEmpty()) {
                out.println("<h3>Nothing is found for the query: <b>" + q + "</b></h3>");
            } else {
                out.println("<h3>There are " + results.size() + " result(s) for the query: '" + q + "'</h3>");
                for (String result : results) {
                    out.println("<div>" + result + "</div>");
                }
            }
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (ParseException ex) {
                Logger.getLogger(SampleSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            out.close();
        }
    }

    public static List<String> search (String indexDir, String q) throws IOException, ParseException {
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexSearcher is = new IndexSearcher(dir);
        
        QueryParser parser = new QueryParser(Version.LUCENE_30, 
            "title",
            new StandardAnalyzer(Version.LUCENE_30));
        
        Query query = parser.parse(q);
        long start = System.currentTimeMillis();
        TopDocs hits = is.search(query, 10);
        long end = System.currentTimeMillis();
        
        System.err.println("Found " + hits.totalHits +
            " document(s) (in " + (end - start) +
            " milliseconds) that matched query '" + 
            q + "':'");
        
        List<String> retFullPaths = new ArrayList();
        
        for (ScoreDoc scoreDoc: hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            // System.out.println(doc.get("fullpath"));
            
            retFullPaths.add(doc.get("title"));
        }
        
        // is.close();
        
        return retFullPaths;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
