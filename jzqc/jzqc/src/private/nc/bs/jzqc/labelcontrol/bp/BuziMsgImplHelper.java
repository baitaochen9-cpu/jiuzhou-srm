package nc.bs.jzqc.labelcontrol.bp;

/*     */
/*     */import java.util.ArrayList;
/*     */
import java.util.HashMap;
/*     */
import java.util.HashSet;
/*     */
import java.util.Iterator;
/*     */
import java.util.List;
/*     */
import java.util.Map;
/*     */
import java.util.Set;

/*     */
import nc.bs.framework.common.InvocationInfoProxy;
/*     */
import nc.bs.framework.common.NCLocator;
/*     */
import nc.bs.logging.Logger;
/*     */
import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*     */
import nc.buzimsg.itf.ISelfDefRcvConvertor;
/*     */
import nc.buzimsg.util.MsgresRegServiceFetcher;
/*     */
import nc.buzimsg.vo.BuziMsgSendingContext;
/*     */
import nc.buzimsg.vo.MsgresRcvConfVO;
/*     */
import nc.buzimsg.vo.MsgresRcvVO;
/*     */
import nc.buzimsg.vo.MsgresTempConfVO;
/*     */
import nc.buzimsg.vo.SelfDefRcvConvertorContext;
/*     */
import nc.buzimsg.vo.SelfDefReceiverVO;
/*     */
import nc.message.templet.bs.MsgContentCreator;
/*     */
import nc.message.vo.MessageVO;
/*     */
import nc.message.vo.NCMessage;
/*     */
import nc.pubitf.rbac.IUserPubService;
/*     */
import nc.vo.ml.LanguageVO;
/*     */
import nc.vo.ml.MultiLangContext;
/*     */
import nc.vo.pub.BusinessException;
/*     */
import nc.vo.sm.UserVO;

/*     */
import org.apache.commons.collections.CollectionUtils;
/*     */
import org.apache.commons.collections.MapUtils;
/*     */
import org.apache.commons.lang.ArrayUtils;
/*     */
import org.apache.commons.lang.StringUtils;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class BuziMsgImplHelper
/*     */{
	/*     */private static String[] allEnableLangCodes;
	/* 44 */private static Map<String, String> selfDefRcvImplFullClassPathMap = new HashMap();

	/*     */
	/*     */public static void sendingContextLeagalityCheck(
			BuziMsgSendingContext context) throws BusinessException
	/*     */{
		/* 48 */String msgrescode = context.getMsgrescode();
		/* 49 */if (StringUtils.isEmpty(msgrescode)) {
			/* 50 */throw new BusinessException(
					"Message resource code is invalid!");
			/*     */}
		/*     */}

	/*     */
	/*     */public static String[] getAllEnabledLangCodes()
	/*     */{
		/* 56 */if (ArrayUtils.isEmpty(allEnableLangCodes))
		/*     */{
			/* 58 */LanguageVO[] langvos = getAllEnabledLangVO();
			/* 59 */if (ArrayUtils.isEmpty(langvos)) {
				/* 60 */return null;
				/*     */}
			/* 62 */String[] langcodes = new String[langvos.length];
			/* 63 */for (int i = 0; i < langvos.length; i++) {
				/* 64 */langcodes[i] = langvos[i].getLangcode();
				/*     */}
			/* 66 */allEnableLangCodes = langcodes;
			/*     */}
		/*     */
		/* 69 */return allEnableLangCodes;
		/*     */}

	/*     */
	/*     */
	/*     */public static String getMainLangcode()
	/*     */{
		/* 75 */return getAllEnabledLangCodes()[0];
		/*     */}

	/*     */
	/*     */public static MsgresTempConfVO queryMsgresTempConfVO(
			String msgresCode, String pk_billtypecode)
	/*     */{
		/* 80 */MsgresTempConfVO tempConf = null;
		/*     */
		/*     */try
		/*     */{
			/* 84 */tempConf = MsgresRegServiceFetcher.getQueryService()
					.queryMsgresTempConfVOByCodeAndPk_billtypecode(msgresCode,
							pk_billtypecode, getLoginPk_Group());
			/*     */}
		/*     */catch (BusinessException e)
		/*     */{
			/* 88 */Logger
					.error("Attempt to fetch MsgresTempConfVO failed!", e);
			/*     */}
		/*     */
		/* 91 */return tempConf;
		/*     */}

	/*     */
	/*     */public static MsgresRcvConfVO[] queryMsgresRcvConfVO(
			String msgrescode, String[] pkorgs, String pk_billtypecode)
	/*     */{
		/* 96 */MsgresRcvConfVO[] rcvConfs = null;
		/*     */
		/*     */try
		/*     */{
			/* 100 */rcvConfs = MsgresRegServiceFetcher.getQueryService()
					.queryMsgresRcvConfVO(msgrescode, pkorgs, pk_billtypecode,
							getLoginPk_Group());
			/*     */}
		/*     */catch (BusinessException e)
		/*     */{
			/* 104 */Logger
					.error("Attempt to fetch MsgresRcvConfVO failed!", e);
			/*     */}
		/*     */
		/* 107 */return rcvConfs;
		/*     */}

	/*     */
	/*     */
	/*     */public static Map<String, NCMessage> getNCMsgMap(
			MsgresTempConfVO tempConfVO, NCMessage message,
			BuziMsgSendingContext context)
	/*     */throws BusinessException
	/*     */{
		/* 132 */String[] langcodes = getAllEnabledLangCodes();
		/* 133 */if (ArrayUtils.isEmpty(langcodes)) {
			/* 134 */return null;
			/*     */}
		/*     */
		/* 137 */Map<String, NCMessage> ncMsgMap = new MsgContentCreator()
				.createMessageUsingTemp(tempConfVO.getMsgtempcode(),
						getPk_group(tempConfVO), langcodes, message,
						context.getCalculater(), context.getBillVO(),
						context.getDatasource());
		/*     */
		/*     */
		/*     */
		/*     */
		/* 142 */return ncMsgMap;
		/*     */}

	/*     */
	/*     */
	/*     */public static String assembleSendType(String msgsendType)
	/*     */{
		/* 148 */if (!msgsendType.contains("+")) {
			/* 149 */return msgsendType;
			/*     */}
		/* 151 */return StringUtils.replace(msgsendType, "+", ",");
		/*     */}

	/*     */
	/*     */public static NCMessage[] getNeedSendNCMessage(String msgsendType,
			Map<String, NCMessage> ncMsgMap, Set<String> userPkSet)
			throws BusinessException
	/*     */{
		/* 156 */UserVO[] users = getUsers(userPkSet);
		/* 157 */if (ArrayUtils.isEmpty(users)) {
			/* 158 */return null;
			/*     */}
		/* 160 */Map<String, List<String>> userListMap = mergeUserByLangcode(users);
		/* 161 */if (MapUtils.isEmpty(userListMap)) {
			/* 162 */return null;
			/*     */}
		/*     */
		/* 165 */Map<String, NCMessage> finalResultMap = new HashMap();
		/*     */
		/*     */
		/* 168 */setMessageInfoAndFillResultMap(msgsendType, ncMsgMap,
				userListMap, finalResultMap);
		/*     */
		/* 170 */if (MapUtils.isEmpty(finalResultMap)) {
			/* 171 */return null;
			/*     */}
		/* 173 */List<NCMessage> messageList = new ArrayList();
		/*     */
		/* 175 */Iterator<String> resultIt = finalResultMap.keySet().iterator();
		/* 176 */while (resultIt.hasNext())
		/*     */{
			/* 178 */String key = (String) resultIt.next();
			/* 179 */messageList.add(finalResultMap.get(key));
			/*     */}
		/*     */
		/* 182 */return (NCMessage[]) messageList.toArray(new NCMessage[0]);
		/*     */}

	/*     */
	/*     */
	/*     */private static void setMessageInfoAndFillResultMap(String msgsendType,
			Map<String, NCMessage> ncMsgMap,
			Map<String, List<String>> userListMap,
			Map<String, NCMessage> finalResultMap)
	/*     */{
		/* 188 */Iterator<String> it = userListMap.keySet().iterator();
		/* 189 */while (it.hasNext())
		/*     */{
			/*     */
			/* 192 */NCMessage message = null;
			/* 193 */String langcode = (String) it.next();
			/*     */
			/* 195 */List<String> userpkList = (List) userListMap.get(langcode);
			/*     */
			/* 197 */if (finalResultMap.containsKey(langcode))
			/*     */{
				/* 199 */message = (NCMessage) finalResultMap.get(langcode);
				/*     */
				/*     */
				/*     */}
			/* 203 */else if (ncMsgMap.containsKey(langcode))
			/*     */{
				/* 205 */message = (NCMessage) ncMsgMap.get(langcode);
				/*     */}
			/*     */else
			/*     */{
				/* 209 */String mainLangcode = getMainLangcode();
				/* 210 */message = (NCMessage) ncMsgMap.get(mainLangcode);
				/*     */}
			/*     */
			/*     */
			/* 214 */if (message != null)
			/*     */{
				/*     */
				/* 217 */setMulReceiver(message, userpkList);
				/*     */
				/* 219 */message.getMessage().setMsgtype(msgsendType);
				/*     */
				/* 221 */finalResultMap.put(langcode, message);
				/*     */}
			/*     */}
		/*     */}

	/*     */
	/*     */public static boolean isEnabled(MsgresTempConfVO tempConfVO)
	/*     */{
		/* 228 */return tempConfVO.getEnablestatus().intValue() == 2;
		/*     */}

	/*     */
	/*     */public static void fillUserPkSet(Set<String> userPkSet,
			MsgresRcvConfVO[] rcvConfs, Object billVO) throws BusinessException
	/*     */{
		/* 234 */if ((ArrayUtils.isEmpty(rcvConfs))) {
			/* 235 */return;
			/*     */}
		/*     */
		/* 241 */Set<String> fromConf = obtainUserpkSet(rcvConfs, billVO);
		/* 242 */if (!CollectionUtils.isEmpty(fromConf)) {
			/* 243 */userPkSet.addAll(fromConf);
			/*     */}
		/*     */
		/*     */
		/* 260 */if (CollectionUtils.isEmpty(userPkSet)) {
			/* 261 */return;
			/*     */}
		/*     */}

	/*     */
	/*     */private static Set<String> obtainUserpkSet(MsgresRcvConfVO[] rcvConfs,
			Object billVO) throws BusinessException
	/*     */{
		/* 268 */if (ArrayUtils.isEmpty(rcvConfs)) {
			/* 269 */return null;
			/*     */}
		/* 271 */Set<String> totalSet = new HashSet();
		/*     */
		/*     */
		/* 274 */Set<String> nonSelfDefTotalSet = getNonSelfDefReceiverTypeUserPkSet(rcvConfs);
		/* 275 */if (!CollectionUtils.isEmpty(nonSelfDefTotalSet)) {
			/* 276 */totalSet.addAll(nonSelfDefTotalSet);
			/*     */}
		/*     */
		/* 279 */Set<String> selfDefTotalSet = getSelfDefReceiverTypeUserPkSet(
				rcvConfs, billVO);
		/* 280 */if (!CollectionUtils.isEmpty(selfDefTotalSet)) {
			/* 281 */totalSet.addAll(selfDefTotalSet);
			/*     */}
		/* 283 */return totalSet;
		/*     */}

	/*     */
	/*     */
	/*     */private static Map<String, List<String>> mergeUserByLangcode(
			UserVO[] users)
	/*     */{
		/* 289 */Map<String, List<String>> map = new HashMap();
		/*     */
		/* 291 */for (UserVO user : users)
		/*     */{
			/* 293 */String langcode = getUserContentLangCode(user);
			/*     */
			/* 295 */if (langcode == null) {
				/* 296 */langcode = getMainLangcode();
				/*     */}
			/* 298 */if (!map.containsKey(langcode)) {
				/* 299 */map.put(langcode, new ArrayList());
				/*     */}
			/* 301 */((List) map.get(langcode)).add(user.getCuserid());
			/*     */}
		/*     */
		/* 304 */if (MapUtils.isEmpty(map)) {
			/* 305 */return null;
			/*     */}
		/* 307 */return map;
		/*     */}

	/*     */
	/*     */private static void setMulReceiver(NCMessage message,
			List<String> userpkList)
	/*     */{
		/* 312 */MessageVO messageVO = message.getMessage();
		/* 313 */StringBuffer sb = new StringBuffer();
		/*     */
		/* 315 */for (int i = 0; i < userpkList.size(); i++)
		/*     */{
			/* 317 */if (i > 0) {
				/* 318 */sb.append(",");
				/*     */}
			/* 320 */sb.append((String) userpkList.get(i));
			/*     */}
		/*     */
		/* 323 */messageVO.setReceiver(sb.toString());
		/*     */}

	/*     */
	/*     */private static UserVO[] getUsers(Set<String> userPkSet)
			throws BusinessException
	/*     */{
		/* 328 */String[] userpks = (String[]) userPkSet.toArray(new String[0]);
		/* 329 */return getUserQueryService().getUsersByPKs(userpks);
		/*     */}

	/*     */
	/* 332 */static IUserPubService userQuery = null;

	/*     */
	/*     */private static IUserPubService getUserQueryService() {
		/* 335 */if (userQuery == null)
			/* 336 */userQuery = (IUserPubService) NCLocator.getInstance()
					.lookup(IUserPubService.class);
		/* 337 */return userQuery;
		/*     */}

	/*     */
	/*     */private static String getPk_group(MsgresTempConfVO tempConfVO)
	/*     */{
		/* 342 */if ("Y".equals(tempConfVO.getIsmsgtempglobal().trim()))
			/* 343 */return "GLOBLE00000000000000";
		/* 344 */return InvocationInfoProxy.getInstance().getGroupId();
		/*     */}

	/*     */
	/*     */private static Set<String> getNonSelfDefReceiverTypeUserPkSet(
			MsgresRcvConfVO[] rcvConfs) throws BusinessException
	/*     */{
		/* 358 */Map<Integer, List<String>> nonSefDefRcvrcvPksMap = getNonSelDefRcvClassifiedReceiverPks(rcvConfs);
		/* 359 */if (MapUtils.isEmpty(nonSefDefRcvrcvPksMap)) {
			/* 360 */return null;
			/*     */}
		/* 362 */Set<String> nonTotalSet = new HashSet();
		/* 363 */Iterator<Integer> it = nonSefDefRcvrcvPksMap.keySet()
				.iterator();
		/* 364 */while (it.hasNext())
		/*     */{
			/* 366 */Integer receiverType = (Integer) it.next();
			/* 367 */String[] receiverpks = (String[]) ((List) nonSefDefRcvrcvPksMap
					.get(receiverType)).toArray(new String[0]);
			/* 368 */INonSelfDefReceiverParser parser = NonSelfDefReceiverParserFactory
					.getINonSelfDefReceiverParser(receiverType.intValue());
			/*     */
			/* 370 */Set<String> set = parser.getUserPks(receiverpks);
			/* 371 */if (!CollectionUtils.isEmpty(set)) {
				/* 372 */nonTotalSet.addAll(set);
				/*     */}
			/*     */}
		/* 375 */return nonTotalSet;
		/*     */}

	/*     */
	/*     */
	/*     */private static Map<Integer, List<String>> getNonSelDefRcvClassifiedReceiverPks(
			MsgresRcvConfVO[] rcvConfs)
	/*     */{
		/* 381 */Map<Integer, List<String>> nonSefDefRcvrcvPksMap = new HashMap();
		/*     */
		/* 383 */for (MsgresRcvConfVO rcvConf : rcvConfs)
		/*     */{
			/* 385 */MsgresRcvVO[] rcvs = rcvConf.getReceivers();
			/* 386 */if (!ArrayUtils.isEmpty(rcvs))
			/*     */{
				/*     */
				/* 389 */for (MsgresRcvVO rcv : rcvs)
				/*     */{
					/* 391 */Integer receiverType = rcv.getReceivertype();
					/* 392 */if (0 != receiverType.intValue())
					/*     */{
						/*     */
						/* 395 */if (!nonSefDefRcvrcvPksMap
								.containsKey(receiverType)) {
							/* 396 */nonSefDefRcvrcvPksMap.put(receiverType,
									new ArrayList());
							/*     */}
						/* 398 */((List) nonSefDefRcvrcvPksMap
								.get(receiverType)).add(rcv.getReceiverpk());
						/*     */}
					/*     */}
			}
			/*     */}
		/* 402 */return nonSefDefRcvrcvPksMap;
		/*     */}

	/*     */
	/*     */private static Set<String> getSelfDefReceiverTypeUserPkSet(
			MsgresRcvConfVO[] rcvConfs, Object billVO) throws BusinessException
	/*     */{
		/* 407 */List<SelfDefRcvConvertorContextWrapper> contextWraperList = genContextWrapperList(
				rcvConfs, billVO);
		/* 408 */if (CollectionUtils.isEmpty(contextWraperList)) {
			/* 409 */return null;
			/*     */}
		/* 411 */addData2SelfDefRcvImplFullClassPathMap(contextWraperList);
		/*     */
		/* 413 */Set<String> totalSet = new HashSet();
		/* 414 */for (SelfDefRcvConvertorContextWrapper wrapper : contextWraperList)
		/*     */{
			/* 416 */String receiverPk = wrapper.getReceiverPk();
			/* 417 */String fullClassName = getFulClassNameByReceiverPk(receiverPk);
			/* 418 */if (!StringUtils.isEmpty(fullClassName))
			/*     */{
				/*     */
				/* 421 */ISelfDefRcvConvertor convertor = genImplByFullClassName(fullClassName);
				/* 422 */SelfDefRcvConvertorContext context = wrapper
						.getContext();
				/* 423 */Set<String> set = convertor.convert(context);
				/*     */
				/* 425 */if (!CollectionUtils.isEmpty(set))
					/* 426 */totalSet.addAll(set);
				/*     */}
			/*     */}
		/* 429 */return totalSet;
		/*     */}

	/*     */
	/*     */private static List<SelfDefRcvConvertorContextWrapper> genContextWrapperList(
			MsgresRcvConfVO[] rcvConfs, Object billVO)
	/*     */{
		/* 434 */List<SelfDefRcvConvertorContextWrapper> wrapperList = new ArrayList();
		/*     */
		/* 436 */for (MsgresRcvConfVO rcvConf : rcvConfs)
		/*     */{
			/* 438 */MsgresRcvVO[] rcvs = rcvConf.getReceivers();
			/* 439 */if (!ArrayUtils.isEmpty(rcvs))
			/*     */{
				/*     */
				/* 442 */String msgrescode = rcvConf.getMsgres_code();
				/* 443 */String pkorg = rcvConf.getPk_org();
				/* 444 */String pk_billtypecode = rcvConf.getPk_billtypecode();
				/*     */
				/* 446 */for (MsgresRcvVO rcv : rcvs)
				/*     */{
					/* 448 */Integer receiverType = rcv.getReceivertype();
					/* 449 */if (0 == receiverType.intValue())
					/*     */{
						/* 451 */String receiverPk = rcv.getReceiverpk();
						/* 452 */if (!StringUtils.isEmpty(receiverPk))
						/*     */{
							/*     */
							/* 455 */SelfDefRcvConvertorContext context = createSelfDefRcvConvertorContext(
									billVO, msgrescode, pkorg, pk_billtypecode);
							/* 456 */SelfDefRcvConvertorContextWrapper wrapper = createWrapper(
									context, receiverPk);
							/* 457 */wrapperList.add(wrapper);
							/*     */}
						/*     */}
					/*     */}
				/*     */}
			/*     */}
		/* 463 */return wrapperList;
		/*     */}

	/*     */
	/*     */private static SelfDefRcvConvertorContext createSelfDefRcvConvertorContext(
			Object billVO, String msgrescode, String pkorg,
			String pk_billtypecode)
	/*     */{
		/* 468 */SelfDefRcvConvertorContext context = new SelfDefRcvConvertorContext();
		/* 469 */context.setBillVO(billVO);
		/* 470 */context.setMsgrescode(msgrescode);
		/* 471 */context.setPkorg(pkorg);
		/* 472 */context.setPk_billtypecode(pk_billtypecode);
		/* 473 */return context;
		/*     */}

	/*     */
	/*     */private static SelfDefRcvConvertorContextWrapper createWrapper(
			SelfDefRcvConvertorContext context, String receiverPk)
	/*     */{
		/* 478 */SelfDefRcvConvertorContextWrapper wrapper = new SelfDefRcvConvertorContextWrapper();
		/* 479 */wrapper.setReceiverPk(receiverPk);
		/* 480 */wrapper.setContext(context);
		/* 481 */return wrapper;
		/*     */}

	/*     */
	/*     */private static void addData2SelfDefRcvImplFullClassPathMap(
			List<SelfDefRcvConvertorContextWrapper> list)
			throws BusinessException
	/*     */{
		/* 486 */List<String> unAddedPks = new ArrayList();
		/* 487 */for (SelfDefRcvConvertorContextWrapper wrapper : list)
		/*     */{
			/* 489 */String receiverpk = wrapper.getReceiverPk();
			/* 490 */if (!selfDefRcvImplFullClassPathMap
					.containsKey(receiverpk)) {
				/* 491 */unAddedPks.add(receiverpk);
				/*     */}
			/*     */}
		/* 494 */if (CollectionUtils.isEmpty(unAddedPks)) {
			/* 495 */return;
			/*     */}
		/* 497 */SelfDefReceiverVO[] receivers = MsgresRegServiceFetcher
				.getQueryService().querySelfDefReceiverVOByPks(
						(String[]) unAddedPks.toArray(new String[0]));
		/*     */
		/* 499 */if (ArrayUtils.isEmpty(receivers)) {
			/* 500 */return;
			/*     */}
		/* 502 */for (SelfDefReceiverVO receiver : receivers) {
			/* 503 */selfDefRcvImplFullClassPathMap.put(
					receiver.getPk_selfdef_rcv(),
					receiver.getCnvtorimplfullpath());
			/*     */}
		/*     */}

	/*     */
	/*     */private static String getFulClassNameByReceiverPk(String receiverPk) {
		/* 508 */return (String) selfDefRcvImplFullClassPathMap.get(receiverPk);
		/*     */}

	/*     */
	/*     */private static ISelfDefRcvConvertor genImplByFullClassName(
			String className)
	/*     */{
		/* 513 */String trimed = StringUtils.trim(className);
		/*     */
		/* 515 */if (StringUtils.isEmpty(trimed)) {
			/* 516 */return null;
			/*     */}
		/*     */try
		/*     */{
			/* 520 */return (ISelfDefRcvConvertor) Class.forName(trimed)
					.newInstance();
			/*     */}
		/*     */catch (Exception e)
		/*     */{
			/* 524 */Logger
					.error("Attempt to new an instance of ISelfDefRcvConvertor failed!",
							e);
			/*     */}
		/*     */
		/* 527 */return null;
		/*     */}

	/*     */
	/*     */private static LanguageVO[] getAllEnabledLangVO()
	/*     */{
		/* 532 */LanguageVO[] langvos = MultiLangContext.getInstance()
				.getEnableLangVOs();
		/*     */
		/* 534 */if (ArrayUtils.isEmpty(langvos)) {
			/* 535 */return null;
			/*     */}
		/* 537 */return langvos;
		/*     */}

	/*     */
	/*     */private static String getUserContentLangCode(UserVO user)
	/*     */{
		/* 542 */String pk_langcode = user.getContentlang();
		/* 543 */LanguageVO[] all = getAllEnabledLangVO();
		/*     */
		/* 545 */if (ArrayUtils.isEmpty(all)) {
			/* 546 */return null;
			/*     */}
		/* 548 */for (LanguageVO languageVO : all)
		/*     */{
			/* 550 */if (languageVO.getPk_multilang().equals(pk_langcode)) {
				/* 551 */return languageVO.getLangcode();
				/*     */}
			/*     */}
		/* 554 */return null;
		/*     */}

	/*     */
	/*     */private static String getLoginPk_Group()
	/*     */{
		/* 559 */return InvocationInfoProxy.getInstance().getGroupId();
		/*     */}
	/*     */
}

/*
 * Location:
 * D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name: nc.buzimsg.impl.BuziMsgImplHelper Java Class Version: 7
 * (51.0) JD-Core Version: 0.7.1
 */