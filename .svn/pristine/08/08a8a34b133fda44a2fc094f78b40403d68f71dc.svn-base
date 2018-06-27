package com.csii.next.monitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.csii.next.monitor.core.Utils;
import com.csii.next.monitor.trace.Trace;
import com.csii.next.monitor.trace.TraceContext;

public class TraceInterceptor implements HandlerInterceptor {
  private Trace trace;
  private MonitorConfig monitorConfig = MonitorConfig.getInstance();

  public void afterCompletion(HttpServletRequest req, HttpServletResponse arg1, Object arg2,
      Exception exception) throws Exception {
    // 新增链路收集
    collect(trace, req, exception);
    // 移除资源
    TraceContext.removeContext();
    if (trace != null) {
      trace = null;
    }
  }

  public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
      ModelAndView arg3) throws Exception {
    // TODO Auto-generated method stub

  }

  public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2)
      throws Exception {
    // 新增NTC链路记录
    trace = initTrace(req);
    return true;
  }

  public Trace initTrace(HttpServletRequest req) {
    trace = new Trace();
    // 设置请求数据
    // trace.setRequest(getRequest(context,trace));
    trace.setStartTime(System.currentTimeMillis());
    trace.setSide(Trace.SIDE_SERVER);// server
    // 取JmxMonitorServer配置的application应用名
    trace.setServerAppName(monitorConfig.getApplication());
    trace.setCallType(Trace.CALL_TYPE_SERVICE);
    String clientAppName = null;
    trace.setClientIp(req.getRemoteHost());
    // 取客户端应用名称，优先获取context中指定字段Trace.CLIENT_APP_NAME的值，若取不到则获取渠道Id，比如TCP
    clientAppName = req.getParameter(Trace.CLIENT_APP_NAME);
    if (clientAppName == null) {
      // clientAppName=clientInfo.getChannelId();
      clientAppName = "web";
    }
    trace.setClientAppName(clientAppName);
    trace.setProtocol("http");

    trace.setStartCpuTime(Utils.getCurrentThreadCpuTime());

    trace.setServerIp(Utils.getLocalIp4cache());
    trace.setServiceName(req.getRequestURI());


    // 判断是否系统之间的数据传递
    String traceId = req.getParameter(Trace.TRACE_ID);
    String callId = req.getParameter(Trace.CALL_ID);
    if (traceId == null || traceId.equals("")) {
      traceId = Utils.nextId();
      callId = Trace.CALL_ID_FIRST;
    }
    trace.setTraceId(traceId);
    trace.setCallId(callId);
    /*
     * if(context.getUser()!=null){ trace.setUserId(context.getUser().getUserId()); }
     */

    // 创建TraceContext并设置到本地线程变量，目的为了同一系统内部的数据传递
    TraceContext traceContext = new TraceContext();
    traceContext.setTraceId(traceId);
    traceContext.setCallId(callId);
    traceContext.setServiceName(trace.getServiceName());
    traceContext.setClientAppName(clientAppName);
    TraceContext.setContext(traceContext);
    return trace;
  }

  public void collect(Trace trace, HttpServletRequest req, Throwable throwable) {

    trace.setCostTime(System.currentTimeMillis() - trace.getStartTime());
    trace.setCostCpuTime(Utils.getCurrentThreadCpuTime() - trace.getStartCpuTime());
    if (throwable != null) {
      trace.setResult(Trace.RESULT_ERROR);
      trace.setErrorMessage(throwable.getMessage());
      trace.setErrorDetail(Utils.formatThrowable(throwable));
    } else {
      trace.setResult(Trace.RESULT_SUCCESS);
      // trace.setResponse(context.getDataMap());
    }

    MonitorRegistryFactory.getMonitorRegistry().trace(trace);

  }

}
