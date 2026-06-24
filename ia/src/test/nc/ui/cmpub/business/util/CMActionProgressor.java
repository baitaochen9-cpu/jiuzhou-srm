package nc.ui.cmpub.business.util;

import java.lang.reflect.Method;

import javax.swing.SwingWorker;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.progress.IProgressMonitor;
import nc.ui.pub.beans.progress.NCProgresses;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.components.progress.TPAProgressUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.Log;
import nc.vo.uif2.LoginContext;

/**
 * 任务执行监控</br>
 * NCAction 默认有processExceptionHandler,如果其他类使用这个监控器,一定要有异常处理processExceptionHandler这个方法.
 * 
 * @since 6.0
 * @version 2012-6-15 下午03:17:03
 * @author hupeng
 */
public class CMActionProgressor {
    Object[] args;

    Object target;

    String taskmethodname;

    private LoginContext context;

    public LoginContext getContext() {
        return this.context;
    }

    public void setContext(LoginContext context) {
        this.context = context;
    }

    /**
     * 进度条处理工具构造
     * 
     * @param target 执行任务的方法所在对象
     * @param taskmethodname 执行任务的方法名
     */
    public CMActionProgressor(Object target, String taskmethodname) {
        this(target, taskmethodname, new Object[0]);
    }

    /**
     * 进度条处理工具构造
     * 
     * @param target 执行任务的方法所在对象
     * @param taskmethodname 执行任务的方法名
     * @param args 执行任务的方法参数
     */
    public CMActionProgressor(Object target, String taskmethodname, Object... args) {
        this.target = target;
        this.taskmethodname = taskmethodname;
        this.args = args;
    }

    /**
     * 最简单的不可预知任务
     * 
     * @param progressName 进度条显示信息
     */
    public void process(String progressName) {
        Method task = this.getTaskMethod();
        Method exhandler = this.getExceptionHandleMethod();
        // 获得进度任务监控器
        IProgressMonitor progressMonitor = null;
        if (this.getContext() == null) {
            progressMonitor = NCProgresses.createDialogProgressMonitor(null);
        }
        else {
            TPAProgressUtil tpaProgressUtil = new TPAProgressUtil();
            tpaProgressUtil.setContext(this.getContext());
            progressMonitor = tpaProgressUtil.getTPAProgressMonitor();
        }
        // 没有异常处理方法则不执行此监控
        if (exhandler != null && task != null) {
            this.startThread(progressName, task, exhandler, progressMonitor);
        }
    }

    /**
     * @param name
     * @param context
     */
    public void process(String name, LoginContext loginContext) {
        this.setContext(loginContext);
        this.process(name);

    }

    private Method getExceptionHandleMethod() {
        Method exhandlertmp = null;
        try {
            Class<?>[] parameterTypes = new Class<?>[1];
            parameterTypes[0] = Exception.class;
            this.target.getClass().getSuperclass();

            // 支持获取私有方法. 不支持继承
            exhandlertmp =
                    this.getNCACtionClass(this.target.getClass()).getDeclaredMethod("processExceptionHandler",
                            parameterTypes);
            exhandlertmp.setAccessible(true);

        }
        catch (Exception ex) {
            ExceptionUtils.wrappException(ex);
        }
        return exhandlertmp;
    }

    /**
     * 获取NCAction 的 Class.
     * 如果不是NCAction. 直接返回自己的Class类对象
     * 
     * @param clazz
     * @return
     */
    private Class<? extends Object> getNCACtionClass(Class<? extends Object> clazz) {

        Class<?> tempClazz = clazz;
        while (true) {
            String clazzname = tempClazz.getName();
            if (clazzname.equals(Object.class.getName())) {
                break;
            }

            if (clazzname.equals(NCAction.class.getName())) {
                return NCAction.class;
            }
            tempClazz = tempClazz.getSuperclass();

        }

        return clazz;
    }

    private Method getTaskMethod() {
        Method tasktmp = null;
        try {
            Class<?>[] parameterTypes = new Class<?>[this.args.length];
            for (int i = 0; i < this.args.length; i++) {
                parameterTypes[i] = this.args[i].getClass();
            }
            tasktmp = this.target.getClass().getDeclaredMethod(this.taskmethodname, parameterTypes);
            tasktmp.setAccessible(true);
        }
        catch (Exception ex) {
            ExceptionUtils.wrappException(ex);
        }

        return tasktmp;
    }

    private void startThread(final String progressName, final Method task, final Method exhandler,
            final IProgressMonitor progressMonitor) {

        /* "任务执行" pd__pub组件 */
        String name = NCLangRes.getInstance().getStrByID("5001003_0", "05001003-0104");
        progressMonitor.beginTask(name, IProgressMonitor.UNKNOWN_TOTAL_TASK);
        progressMonitor.setProcessInfo(progressName);

        SwingWorker<Object[], Object> sw = new SwingWorker<Object[], Object>() {

            @Override
            protected Object[] doInBackground() throws Exception {
                try {
                    task.invoke(CMActionProgressor.this.target, CMActionProgressor.this.args);
                }
                catch (Exception ex) {
                    Log.error(ex);
                    // TODO:先用这种方式，不然异常抛不到界面
                    try {
                        Throwable cause = ExceptionUtils.unmarsh(ex);
                        // 异常处理
                        exhandler.invoke(CMActionProgressor.this.target, cause);
                    }
                    catch (Exception e1) {
                        ExceptionUtils.wrappException(e1);
                    }

                }
                return null;
            }

            @Override
            protected void done() {
                progressMonitor.done();

            }
        };
        sw.execute();
    }

}
