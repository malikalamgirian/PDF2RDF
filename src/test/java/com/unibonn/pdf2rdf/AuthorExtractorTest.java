/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unibonn.pdf2rdf;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.pdfbox.text.TextPosition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wasif Altaf
 */
public class AuthorExtractorTest {
    
    public AuthorExtractorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAuthorsAsString method, of class AuthorExtractor.
     */
    @Test
    public void testGetAuthorsAsString() throws Exception {
        System.out.println("getAuthorsAsString");
        
        AuthorExtractor instance = new AuthorExtractor();
        String fileNamePathWithExtension = "src\\test\\java\\com\\unibonn\\pdf2rdf\\" 
                + "test.pdf";
        String expResult = "Duhai Alshukaili(B), Alvaro A.A. Fernandes, and Norman W. Paton";
        instance.setFileNamePathWithExtension(fileNamePathWithExtension);
        String result = instance.getAuthorsAsString();
        
        assertEquals(expResult, result);
        
        instance = new AuthorExtractor();
        fileNamePathWithExtension = "src\\test\\java\\com\\unibonn\\pdf2rdf\\" 
                  + "test.pdf";
        expResult = "School of Computer Science, University Manchester, Oxford Road, Manchester M13 9PL, UK";
        instance.setFileNamePathWithExtension(fileNamePathWithExtension);
        result = instance.getAuthorsAffiliationsAsString();

        assertEquals(expResult, result);
        
        instance = new AuthorExtractor();
        fileNamePathWithExtension = "src\\test\\java\\com\\unibonn\\pdf2rdf\\" 
                  + "test.pdf";
        expResult = "{dhahi.alshekaili,a.fernandes,norman.paton}@manchester.ac.uk";
        instance.setFileNamePathWithExtension(fileNamePathWithExtension);
        result = instance.getAuthorsContactsAsString();

        assertEquals(expResult, result);
        
        instance = new AuthorExtractor();
        fileNamePathWithExtension = "src\\test\\java\\com\\unibonn\\pdf2rdf\\" 
                + "splnproc1703.pdf";
        expResult = "First Author1[0000-1111-2222-3333] and Second Author2[1111-2222-3333-4444]";
        instance.setFileNamePathWithExtension(fileNamePathWithExtension);
        result = instance.getAuthorsAsString();;

        assertEquals(expResult, result);
    }


}
