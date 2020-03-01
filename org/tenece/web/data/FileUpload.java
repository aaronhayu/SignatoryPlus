/*
 * FileUpload.java
 *
 * Created on June 25, 2009, 1:49 PM
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.tenece.web.data;

/**
 * Tenece Professional Services, Nigeria
 * @author strategiex
 */
public class FileUpload {
    
    private byte[] bytes;
    private long size;
    private String orginalFileName;
    private String fileName;
    private String absolutePath;
    private String contentType;
    
    /** Creates a new instance of FileUpload */
    public FileUpload() {
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOrginalFileName() {
        return orginalFileName;
    }

    public void setOrginalFileName(String orginalFileName) {
        this.orginalFileName = orginalFileName;
    }
    
    
}
