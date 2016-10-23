package lianjiao.sz

import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import us.codecraft.webmagic.ResultItems
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.Pipeline
import us.codecraft.webmagic.utils.FilePersistentBase

import com.alibaba.fastjson.JSON

/**
 * Store results to files in CVS format.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
class CSVFilePipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * new JsonFilePageModelPipeline with default path "/data/webmagic/"
     */
    public CSVFilePipeline() {
        setPath("/data/webmagic");
    }

    public CSVFilePipeline(String path) {
        setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
		if(resultItems.isSkip()) {
			return;
		}
        String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".csv")));
			String str = resultItems.get(SoldDataProcessor.KEY_SOLDDATA).toString();
            printWriter.write(str.substring(str.indexOf('(') + 1, str.length() - 1) + '\r\n');
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}
