/**
 * 
 */
package ru.webmoney.api.req;

import ru.webmoney.api.dict.Wmid;

/**
 * Интерфейс X16: Создание кошелька.
 * 
 * @author Alex Gusev <flancer64@gmail.com>
 * @version 1.0
 * 
 */
public class X16Request extends AbstractRequest {
	/**
	 * Текстовое название кошелька, которое будет отображаться в интерфейсе
	 * Webmoney Keeper Classic или Light.
	 */
	private String desc;
	/**
	 * Тип создаваемого кошелька в виде одного латинского символа в верхнем
	 * регистре B ,C ,D ,E ,G ,R ,U ,Y ,Z.
	 */
	private char purseType;
	/**
	 * ВМ-идентификатор, которому будет принадлежать вновь созданный кошелек.
	 * Фактически данный ВМ-идентификатор должен быть равен идентификатору
	 * передаваемому в теге wmid идентификатора подписывающего запрос, так как
	 * кошелек может быть создан только у идентификатора подписывающего запрос,
	 * работа с интерфейсом по доверию невозможна.
	 */
	private Wmid wmid;

	/**
	 * Текстовое название кошелька, которое будет отображаться в интерфейсе
	 * Webmoney Keeper Classic или Light.
	 * 
	 * @return Текстовое название кошелька, которое будет отображаться в
	 *         интерфейсе Webmoney Keeper Classic или Light.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Тип создаваемого кошелька в виде одного латинского символа в верхнем
	 * регистре B ,C ,D ,E ,G ,R ,U ,Y ,Z.
	 * 
	 * @return Тип создаваемого кошелька в виде одного латинского символа в
	 *         верхнем регистре B ,C ,D ,E ,G ,R ,U ,Y ,Z.
	 */
	public char getPurseType() {
		return purseType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lv.flancer.wmt.xml.req.XmlRequest#getTextToSign()
	 */
	@Override
	public String getTextToSign() {
		String result = "";
		result += this.wmid;
		result += this.purseType;
		result += this.requestNum;
		return result;
	}

	/**
	 * ВМ-идентификатор, которому будет принадлежать вновь созданный кошелек.
	 * Фактически данный ВМ-идентификатор должен быть равен идентификатору
	 * передаваемому в теге wmid идентификатора подписывающего запрос, так как
	 * кошелек может быть создан только у идентификатора подписывающего запрос,
	 * работа с интерфейсом по доверию невозможна.
	 * 
	 * @return ВМ-идентификатор, которому будет принадлежать вновь созданный
	 *         кошелек. Фактически данный ВМ-идентификатор должен быть равен
	 *         идентификатору передаваемому в теге wmid идентификатора
	 *         подписывающего запрос, так как кошелек может быть создан только у
	 *         идентификатора подписывающего запрос, работа с интерфейсом по
	 *         доверию невозможна.
	 */
	public Wmid getWmid() {
		return wmid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lv.flancer.wmt.xml.req.XmlRequest#getXmlRequest()
	 */
	@Override
	public String getXmlRequest() {
		String result = "<?xml version=\"1.0\"  encoding=\"windows-1251\"?>";
		result += "<w3s.request>";
		result += "<reqn>" + this.requestNum + "</reqn>";
		if (this.signerWmid != null) {
			result += "<wmid>" + this.signerWmid + "</wmid>";
		} else {
			result += "<wmid />";
		}
		if (this.sign != null) {
			result += "<sign>" + this.sign + "</sign>";
		} else {
			result += "<sign />";
		}
		result += "<createpurse>";
		result += "<wmid>" + this.wmid + "</wmid>";
		result += "<pursetype>" + this.purseType + "</pursetype>";
		result += "<desc>" + this.desc + "</desc>";
		result += "</createpurse>";
		result += "</w3s.request>";
		return result;
	}

	/**
	 * Текстовое название кошелька, которое будет отображаться в интерфейсе
	 * Webmoney Keeper Classic или Light.
	 * 
	 * @param desc
	 *            Текстовое название кошелька, которое будет отображаться в
	 *            интерфейсе Webmoney Keeper Classic или Light.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Тип создаваемого кошелька в виде одного латинского символа в верхнем
	 * регистре B ,C ,D ,E ,G ,R ,U ,Y ,Z.
	 * 
	 * @param purseType
	 *            Тип создаваемого кошелька в виде одного латинского символа в
	 *            верхнем регистре B ,C ,D ,E ,G ,R ,U ,Y ,Z.
	 */
	public void setPurseType(char purseType) {
		this.purseType = Character.toUpperCase(purseType);
	}

	/**
	 * ВМ-идентификатор, которому будет принадлежать вновь созданный кошелек.
	 * Фактически данный ВМ-идентификатор должен быть равен идентификатору
	 * передаваемому в теге wmid идентификатора подписывающего запрос, так как
	 * кошелек может быть создан только у идентификатора подписывающего запрос,
	 * работа с интерфейсом по доверию невозможна.
	 * 
	 * @param wmid
	 *            ВМ-идентификатор, которому будет принадлежать вновь созданный
	 *            кошелек. Фактически данный ВМ-идентификатор должен быть равен
	 *            идентификатору передаваемому в теге wmid идентификатора
	 *            подписывающего запрос, так как кошелек может быть создан
	 *            только у идентификатора подписывающего запрос, работа с
	 *            интерфейсом по доверию невозможна.
	 */
	public void setWmid(String wmid) {
		this.wmid = new Wmid(wmid);
	}

	/**
	 * ВМ-идентификатор, которому будет принадлежать вновь созданный кошелек.
	 * Фактически данный ВМ-идентификатор должен быть равен идентификатору
	 * передаваемому в теге wmid идентификатора подписывающего запрос, так как
	 * кошелек может быть создан только у идентификатора подписывающего запрос,
	 * работа с интерфейсом по доверию невозможна.
	 * 
	 * @param wmid
	 *            ВМ-идентификатор, которому будет принадлежать вновь созданный
	 *            кошелек. Фактически данный ВМ-идентификатор должен быть равен
	 *            идентификатору передаваемому в теге wmid идентификатора
	 *            подписывающего запрос, так как кошелек может быть создан
	 *            только у идентификатора подписывающего запрос, работа с
	 *            интерфейсом по доверию невозможна.
	 */
	public void setWmid(Wmid wmid) {
		this.wmid = wmid;
	}

}