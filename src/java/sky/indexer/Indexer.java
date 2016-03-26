/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sky.indexer;

import java.io.IOException;

/**
 *
 * @author cnaquoc
 */
public interface Indexer {
    void index(DBLPItem item ) throws IOException ;
    void Finish() throws IOException ;
}
