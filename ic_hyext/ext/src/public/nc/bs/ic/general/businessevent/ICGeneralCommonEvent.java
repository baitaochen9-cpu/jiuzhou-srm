package nc.bs.ic.general.businessevent;

import nc.bs.businessevent.AbstractBusinessEvent;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

public class ICGeneralCommonEvent extends AbstractBusinessEvent
{
  private static final long serialVersionUID = 1L;
  private Object[] newObjs = null;

  private Object[] oldObjs = null;
  private ICEventParameter icEventParameter;

  public ICEventParameter getIcEventParameter()
  {
    return this.icEventParameter;
  }

  public void setIcEventParameter(ICEventParameter icEventParameter) {
    this.icEventParameter = icEventParameter;
  }

  public ICGeneralCommonEvent(String sourceID, String eventType, Object[] objs)
  {
    super(sourceID, eventType);
    setNewObjs(objs);
  }

  public ICGeneralCommonEvent(String sourceID, String eventType, Object[] oldObjs, Object[] newObjs)
  {
    super(sourceID, eventType);
    setOldObjs(oldObjs);
    setNewObjs(newObjs);
  }

  public Object[] getNewObjs()
  {
    return this.newObjs;
  }

  public Object[] getObjs() {
    return this.newObjs;
  }

  public Object[] getOldObjs() {
    return this.oldObjs;
  }

  public ValueObject getUserObject()
  {
    return new ICGeneralCommonUserObj(this.newObjs, this.oldObjs);
  }

  public void setNewObjs(Object[] newObjs) {
    this.newObjs = newObjs;
  }

  public void setObjs(Object[] newObjs) {
    this.newObjs = newObjs;
  }

  public void setOldObjs(Object[] oldObjs) {
    this.oldObjs = oldObjs;
  }

  public static class ICGeneralCommonUserObj extends ValueObject
  {
    private static final long serialVersionUID = 1L;
    private Object newObjects;
    private Object oldObjects;

    private ICGeneralCommonUserObj(Object newObjects, Object oldObjects)
    {
      this.newObjects = newObjects;
      this.oldObjects = oldObjects;
    }

    public String getEntityName()
    {
      return getClass().getName();
    }

    public Object getNewObjects() {
      return this.newObjects;
    }

    public Object getOldObjects() {
      return this.oldObjects;
    }

    public void setNewObjects(Object newObjects) {
      this.newObjects = newObjects;
    }

    public void setOldObjects(Object oldObjects) {
      this.oldObjects = oldObjects;
    }

    public void validate()
      throws ValidationException
    {
    }
  }
}