package cn.ucloud.ufile.api.bucket;

import cn.ucloud.ufile.annotation.UcloudParam;
import cn.ucloud.ufile.auth.BucketAuthorizer;
import cn.ucloud.ufile.bean.BucketResponse;
import cn.ucloud.ufile.exception.UfileParamException;
import cn.ucloud.ufile.exception.UfileRequiredParamNotFoundException;
import cn.ucloud.ufile.http.HttpClient;

/**
 * API-更新Bucket
 *
 * @author: joshua
 * @E-mail: joshua.yin@ucloud.cn
 * @date: 2018/11/12 18:59
 */
public class UpdateBucketApi extends UfileBucketApi<BucketResponse> {
    /**
     * Required
     * 目标Bucket的名称
     */
    @UcloudParam("BucketName")
    private String bucketName;

    /**
     * Required
     * 更新后的Bucket类型
     */
    @UcloudParam("Type")
    private String type;

    /**
     * Optional
     * 相关联的项目ID
     */
    @UcloudParam("ProjectId")
    private String projectId;

    /**
     * 构造方法
     *
     * @param authorizer Bucket授权器
     * @param httpClient Http客户端
     */
    protected UpdateBucketApi(BucketAuthorizer authorizer, HttpClient httpClient) {
        super(authorizer, httpClient, "UpdateBucket");
    }

    /**
     * 配置要更新的bucket名称
     *
     * @param bucketName bucket名称
     * @return {@link UpdateBucketApi}
     */
    public UpdateBucketApi whichBucket(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    /**
     * 配置要更新的bucket类型
     *
     * @param type bucket类型
     * @return {@link UpdateBucketApi}
     */
    public UpdateBucketApi changeType(BucketType type) {
        if (type != null)
            this.type = type.getBucketType();
        return this;
    }

    /**
     * 配置要更新的bucket所属UCloud的projectId
     *
     * @param projectId
     * @return {@link UpdateBucketApi}
     */
    public UpdateBucketApi withProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    protected void parameterValidat() throws UfileParamException {
        super.parameterValidat();
        if (bucketName == null || bucketName.isEmpty())
            throw new UfileRequiredParamNotFoundException(
                    "The required param 'bucketName' can not be null or empty");

        if (type == null || type.isEmpty())
            throw new UfileRequiredParamNotFoundException(
                    "The required param 'type' can not be null or empty");
    }
}
