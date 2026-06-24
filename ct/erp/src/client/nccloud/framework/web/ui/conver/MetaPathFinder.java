package nccloud.framework.web.ui.conver;

import nccloud.framework.core.env.Locator;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.model.meta.IAttributeMeta;
import nccloud.framework.core.model.meta.IBillMeta;
import nccloud.framework.core.model.meta.IMetaResource;
import nccloud.framework.core.model.meta.IVOMeta;
import nccloud.framework.service.meta.vometa.AttributeMeta;
import nccloud.framework.service.meta.vometa.MockAttributeMeta;

/**
 * 元数据路径
 * 
 * @since 2018-6-28 上午10:18:55
 * @version 1.0.0
 * @author 于晓龙
 */
public class MetaPathFinder {

  private IAttributeMeta[] attributes = null;

  private boolean bool = false;

  private int cursor;

  private String[] metas = null;

  private String path;

  private String vometa;

  public MetaPathFinder() {
  }

  /**
   * 构造函数
   * 
   * @param vometa
   * @param path
   */
  public MetaPathFinder(String vometa, String path) {
    this.vometa = vometa;
    this.path = path;
    this.calculate();
  }

  public IAttributeMeta findAttribute(String fullpath, String code) {
    if (fullpath == null) {
      ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("1501003_0", "01501003-0074")/*@res 元数据路径不能为空！有可能异常数据信息 : metapath=*/
          + this.path + "   vometa=" + this.vometa);
    }
    IMetaResource resource = Locator.find(IMetaResource.class);
    IBillMeta docbill = resource.getBillMetaData(fullpath);
    IVOMeta head = docbill.getParent();
    IAttributeMeta attr = head.getAttribute(code);
    if (attr != null) {
      return attr;
    }
    if (docbill.getChildren() == null) {
      return null;
    }
    // 根据元数据关联关系找相应的子表 如bodyfk.num
    for (IVOMeta child : docbill.getChildren()) {
      String relation = docbill.getRelationEndAlias(child);
      if (code.equals(relation)) {
        attr = new MockAttributeMeta();
        ((MockAttributeMeta) attr).setLabel(child.getLabel());
        ((MockAttributeMeta) attr).setName(code);
        ((MockAttributeMeta) attr).setReferencedoc(child.getEntityFullName());
        return attr;
      }
    }
    // 为了兼容视图vo中子表字段有bodyfk字段增加如下逻辑
    for (IVOMeta child : docbill.getChildren()) {
      IAttributeMeta childattr = child.getAttribute(code);
      if (childattr != null) {
        return childattr;
      }
      continue;
    }
    return null;
  }

  public String findAttributeBillBodyMetaID(String fullpath, String code,
      String metapath) {
    if (fullpath == null) {
      ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("1501003_0", "01501003-0074")/*@res 元数据路径不能为空！有可能异常数据信息 : metapath=*/
          + this.path + "vometa=" + this.vometa);
    }
    IMetaResource resource = Locator.find(IMetaResource.class);
    IBillMeta docbill = resource.getBillMetaData(fullpath);
    int indexOf = metapath.indexOf(".");
    if (indexOf >= 0) {
      if (docbill.getChildren() == null) {
        return null;
      }
      for (IVOMeta child : docbill.getChildren()) {
        IAttributeMeta attrch = docbill.getRelationEnd(child);
        if (attrch != null) {
          AttributeMeta att = (AttributeMeta) attrch;
          return att.getEntity().getId();
        }
      }
    }
    IVOMeta head = docbill.getParent();
    IAttributeMeta attr = head.getAttribute(code);
    if (attr != null) {
      AttributeMeta att = (AttributeMeta) attr;
      return att.getEntity().getId();
    }
    if (docbill.getChildren() == null) {
      return null;
    }
    for (IVOMeta child : docbill.getChildren()) {
      IAttributeMeta attrch = docbill.getRelationEnd(child);
      if (attrch != null) {
        AttributeMeta att = (AttributeMeta) attrch;
        return att.getEntity().getId();
      }
    }
    return null;
  }

  public String findAttributeBillMetaID(String fullpath, String code) {
    if (fullpath == null) {
      ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("1501003_0", "01501003-0074")/*@res 元数据路径不能为空！有可能异常数据信息 : metapath=*/
          + this.path + "vometa=" + this.vometa);
    }
    IMetaResource resource = Locator.find(IMetaResource.class);
    IBillMeta docbill = resource.getBillMetaData(fullpath);
    IVOMeta head = docbill.getParent();
    IAttributeMeta attr = head.getAttribute(code);
    if (attr != null) {
      AttributeMeta att = (AttributeMeta) attr;
      return att.getEntity().getId();
    }
    if (docbill.getChildren() == null) {
      return null;
    }
    for (IVOMeta child : docbill.getChildren()) {
      IAttributeMeta attrch = docbill.getRelationEnd(child);
      if (attrch != null) {
        AttributeMeta att = (AttributeMeta) attrch;
        return att.getEntity().getId();
      }
    }
    return null;
  }

  /**
   * 获取metaid
   * 
   * @param fullpath 单据元数据路径
   * @param code 字段编码
   * @param clazz 类路径
   * @return
   */
  public String findMetaID(String fullpath, String code, String clazz) {
    if (fullpath == null) {
      ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl
          .getNCLangRes().getStrByID("1501003_0", "01501003-0074")/*@res 元数据路径不能为空！有可能异常数据信息 : metapath=*/
          + this.path + "vometa=" + this.vometa);
    }
    IMetaResource resource = Locator.find(IMetaResource.class);
    IBillMeta docbill = resource.getBillMetaData(fullpath);
    IVOMeta head = docbill.getParent();
    if (head.getNCVOMeta().getFullclassname().equals(clazz)) {
      IAttributeMeta attr = head.getAttribute(code);
      if (attr != null) {
        AttributeMeta att = (AttributeMeta) attr;
        return att.getEntity().getId();
      }
    }
    if (docbill.getChildren() == null) {
      return null;
    }
    for (IVOMeta child : docbill.getChildren()) {
      if (child.getNCVOMeta().getFullclassname().equals(clazz)) {
        IAttributeMeta attr = child.getAttribute(code);
        if (attr != null) {
          AttributeMeta att = (AttributeMeta) attr;
          return att.getEntity().getId();
        }
      }
    }
    return null;
  }

  /**
   * 获取当前指针所在的属性
   * 
   * @return 元数据属性
   */
  public IAttributeMeta getCurrentAttribute() {
    return this.attributes[this.cursor];
  }

  /**
   * 获取当前指针所在的元数据实体
   * 
   * @return 元数据实体
   */
  public String getCurrentVOMeta1() {
    return this.metas[this.cursor];
  }

  /**
   * 获取最后一个有效的元数据属性
   * 
   * @return 元数据属性
   */
  public IAttributeMeta getLastValidAttribute() {
    IAttributeMeta validAttribute = this.attributes[0];
    int i = 0;
    while (i < this.attributes.length && this.attributes[i] != null) {
      validAttribute = this.attributes[i];
      i++;
    }
    return validAttribute;
  }

  /**
   * 获取最后一个有效的元数据路径
   * 
   * @return 元数据路径
   */
  public String getLastValidMeta() {
    String validMeta = this.metas[0];
    int i = 0;
    while (i < this.metas.length && this.metas[i] != null) {
      validMeta = this.metas[i];
      i++;
    }
    if (validMeta == null) {
      validMeta = this.vometa;
    }
    return validMeta;
  }

  /**
   * @return the path
   */
  public String getPath() {
    return this.path;
  }

  /**
   * 是否有下一个元数据
   * 
   * @return 是否
   */
  public boolean hasNext() {
    return this.cursor < this.metas.length - 1;
  }

  public boolean isBool() {
    return this.bool;
  }

  /**
   * 路径元素指针加一
   */
  public void next() {
    if (!this.hasNext()) {
      String message =
          nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("1501001_0",
              "01501001-0231")/*@res 没有更多的路径元素*/;
      ExceptionUtils.wrapBusinessException(message);
    }
    this.cursor++;
  }

  public void setBool(boolean bool) {
    this.bool = bool;
  }

  private void calculate() {
    String[] names = this.path.split("\\.");
    int size = names.length;
    this.metas = new String[size];
    this.attributes = new IAttributeMeta[size];
    this.attributes[0] = this.findAttribute(this.vometa, names[0]);
    if (this.attributes[0] == null) {
      this.bool = true;
      return;
    }
    this.metas[0] = this.attributes[0].getReferenceDoc();
    if (this.metas[0] == null) {
      this.bool = false;
      return;
    }
    for (int i = 1; i < size; i++) {
      this.attributes[i] = this.findAttribute(this.metas[i - 1], names[i]);
      if (this.attributes[i] == null) {
        break;
      }
      this.metas[i] = this.attributes[i].getReferenceDoc();
    }
  }
}
