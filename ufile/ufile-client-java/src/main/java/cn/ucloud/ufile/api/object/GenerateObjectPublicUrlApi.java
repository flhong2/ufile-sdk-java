package cn.ucloud.ufile.api.object;

import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileParamException;
import cn.ucloud.ufile.exception.UfileRequiredParamNotFoundException;
import cn.ucloud.ufile.http.request.GetRequestBuilder;
import cn.ucloud.ufile.util.Parameter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * API-生成公共下载URL
 *
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018/11/16 12:00
 */
public class GenerateObjectPublicUrlApi {
    private ObjectConfig objectConfig;
    /**
     * Required
     * 云端对象名称
     */
    private String keyName;
    /**
     * Required
     * Bucket空间名称
     */
    private String bucketName;

    /**
     * Ufile 文件下载链接所需的Content-Disposition: attachment文件名
     */
    private String attachmentFileName;

    /**
     * 构造方法
     *
     * @param objectConfig ObjectConfig {@link ObjectConfig}
     * @param keyName      对象名称
     * @param bucketName   bucket名称
     */
    protected GenerateObjectPublicUrlApi(ObjectConfig objectConfig, String keyName, String bucketName) {
        this.objectConfig = objectConfig;
        this.keyName = keyName;
        this.bucketName = bucketName;
    }

    /**
     * 使用Content-Disposition: attachment，并配置attachment的文件名
     *
     * @param attachmentFileName Content-Disposition: attachment的文件名
     * @return
     */
    public GenerateObjectPublicUrlApi withAttachment(String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
        return this;
    }

    /**
     * 使用Content-Disposition: attachment，并且文件名默认为keyName
     *
     * @return
     */
    public GenerateObjectPublicUrlApi withAttachment() {
        this.attachmentFileName = keyName;
        return this;
    }

    /**
     * 生成下载URL
     *
     * @return 下载URL
     * @throws UfileRequiredParamNotFoundException 必要参数未找到时抛出
     */
    public String createUrl() throws UfileClientException {
        parameterValidat();
        GetRequestBuilder builder = (GetRequestBuilder) new GetRequestBuilder()
                .baseUrl(generateFinalHost(bucketName, keyName));

        if (attachmentFileName != null && !attachmentFileName.isEmpty()) {
            try {
                attachmentFileName = URLEncoder.encode(attachmentFileName, "UTF-8").replace("+", "%20");
                builder.addParam(new Parameter("ufileattname", attachmentFileName));
            } catch (UnsupportedEncodingException e) {
                throw new UfileClientException("Occur error during URLEncode attachmentFileName", e);
            }
        }

        return builder.generateGetUrl(builder.getBaseUrl(), builder.getParams());
    }

    private String generateFinalHost(String bucketName, String keyName) throws UfileClientException {
        if (objectConfig == null)
            return null;

        if (objectConfig.isCustomDomain())
            return String.format("%s/%s", objectConfig.getCustomHost(), keyName);

        try {
            bucketName = URLEncoder.encode(bucketName, "UTF-8").replace("+", "%20");
            String region = URLEncoder.encode(objectConfig.getRegion(), "UTF-8")
                    .replace("+", "%20");
            String proxySuffix = URLEncoder.encode(objectConfig.getProxySuffix(), "UTF-8")
                    .replace("+", "%20");
            keyName = URLEncoder.encode(keyName, "UTF-8").replace("+", "%20");
            return new StringBuilder(objectConfig.getProtocol().getValue())
                    .append(String.format("%s.%s.%s/%s", bucketName, region, proxySuffix, keyName)).toString();
        } catch (UnsupportedEncodingException e) {
            throw new UfileClientException("Occur error during URLEncode bucketName and keyName", e);
        }
    }

    protected void parameterValidat() throws UfileParamException {
        if (keyName == null || keyName.isEmpty())
            throw new UfileRequiredParamNotFoundException(
                    "The required param 'keyName' can not be null or empty");

        if (bucketName == null || bucketName.isEmpty())
            throw new UfileRequiredParamNotFoundException(
                    "The required param 'bucketName' can not be null or empty");
    }
}
