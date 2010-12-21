/**
 * 
 */
package ru.webmoney.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Отправляет один запрос (GET или POST) к HTTP-серверу и получает ответ от
 * него. Производит кодирование/декодирование запроса/ответа в соответствующую
 * кодировку (см. {@link #setRequestCharset(Charset)},
 * {@link #setResponseCharset(Charset)}).
 * </p>
 * <p>
 * POST-запрос типа "application/x-www-form-urlencoded", за формирование тела
 * запроса в соответствующем виде отвечает вызывающая сторона (
 * {@link #doPost(String, String)}).
 * </p>
 * <p>
 * Пример "короткого" вызова (настройки по-умолчанию: кодировка запросов -
 * UTF-8; SSL не используется):
 * </p>
 * 
 * <pre>
 * HttpRequester req = new HttpRequester(&quot;secured.host.tld&quot;, 80);
 * String response = req.doGet(&quot;/path/to/get/agent?id=5&quot;);
 * System.out.println(&quot;Response:\n&quot; + response);
 * </pre>
 * 
 * <p>
 * Пример "длинного" вызова (задаются все настройки)
 * </p>
 * 
 * <pre>
 * HttpRequester req = new HttpRequester(&quot;secured.host.tld&quot;, 443);
 * req.setRequestCharset(&quot;windows-1251&quot;);
 * req.setResponseCharset(&quot;windows-1251&quot;);
 * req.setSecuredResuest(true);
 * req.loadTrustStore(&quot;/path/to/truststore.jks&quot;, &quot;TrustStorePassword&quot;);
 * String requestBody = &quot;param1=val1&amp;param2=val2&quot;;
 * String response = req.doPost(&quot;/path/to/post/agent&quot;, requestBody);
 * System.out.println(&quot;Request:\n&quot; + req.getRequest());
 * System.out.println(&quot;Response:\n&quot; + response);
 * </pre>
 * 
 * @author Alex Gusev <flancer64@gmail.com>
 * @version 1.0
 * 
 */
public class HttpRequester {
	private final static String DEFAULT_CAHRSET = "utf-8";

	/**
	 * Адрес сервера для установления соединения.
	 */
	private String host;
	/**
	 * Порт сервера для установления соединения.
	 */
	private int port;
	/**
	 * Запрос, отправленный на сервер, сохраняется для диагностических целей.
	 */
	private String request;
	/**
	 * Кодировка, в которую перекодируются из UTF-8 символы запроса при отправке
	 * на сервер.
	 */
	private String requestCharset;
	/**
	 * Кодировка, из которой перекодируются в UTF-8 символы ответа при получении
	 * их с сервера.
	 */
	private String responseCharset;
	/**
	 * Флаг, указывающий на необходимость использования шифрования (SSL) при
	 * выполнении запроса.
	 */
	private boolean securedResuest;

	/**
	 * Создает экземпляр класса с кодировкой UTF-8 для запроса/ответа.
	 * 
	 * @param host
	 *            Адрес сервера для установления соединения.
	 * @param port
	 *            Порт сервера для установления соединения.
	 * @throws UnknownHostException
	 */
	public HttpRequester(String host, int port) {
		this.host = host;
		this.port = port;
		this.requestCharset = DEFAULT_CAHRSET;
		this.responseCharset = DEFAULT_CAHRSET;
		this.securedResuest = false;
	}

	/**
	 * Посылает серверу GET-запрос по адресу requestAddress.
	 * 
	 * @param requestAddress
	 * @return http-ответ, полученный от сервера
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public String doGet(String requestAddress) throws IOException {
		return this.doRequest(requestAddress, null);
	}

	/**
	 * Посылает серверу POST-запрос по адресу requestAddress, переменные запроса
	 * содержатся в requestBody, за соответствующий вид которого отвечает
	 * вызывающая сторона
	 * 
	 * @param requestAddress
	 * @return http-ответ, полученный от сервера
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public String doPost(String requestAddress, String requestBody)
			throws IOException {
		return this.doRequest(requestAddress, requestBody);
	}

	/**
	 * Выполняет запрос в зависимости от его типа (GET или POST).
	 * 
	 * @param requestAddress
	 * @param requestBody
	 * @param requestType
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private String doRequest(String requestAddress, String requestBody)
			throws IOException {

		String address = (securedResuest ? "https://" : "http://") + host
				+ requestAddress;
		URL url = new URL(address);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		if (null != requestBody) {
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			OutputStreamWriter writer = null;

			try {
				writer = new OutputStreamWriter(connection.getOutputStream(),
						getRequestCharset());
				writer.write(requestBody);
				writer.close();
			} finally {
				if (null != writer)
					writer.close();
			}
		}

		BufferedReader reader = null;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), getResponseCharset()));

			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} finally {
			if (null != reader)
				reader.close();
		}
		reader.close();

		return stringBuilder.toString();
	}

	/**
	 * Адрес сервера для установления соединения.
	 * 
	 * @return Адрес сервера для установления соединения.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Порт сервера для установления соединения.
	 * 
	 * @return Порт сервера для установления соединения.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Запрос, отправленный на сервер, сохраняется для диагностических целей.
	 * 
	 * @return the request Запрос, отправленный на сервер, сохраняется для
	 *         диагностических целей.
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * Кодировка, в которую перекодируются из UTF-8 символы запроса при отправке
	 * на сервер.
	 * 
	 * @return Кодировка, в которую перекодируются из UTF-8 символы запроса при
	 *         отправке на сервер.
	 */
	public String getRequestCharset() {
		return requestCharset;
	}

	/**
	 * Кодировка, из которой перекодируются в UTF-8 символы ответа при получении
	 * их с сервера.
	 * 
	 * @return Кодировка, из которой перекодируются в UTF-8 символы ответа при
	 *         получении их с сервера.
	 */
	public String getResponseCharset() {

		if (null == responseCharset)
			return DEFAULT_CAHRSET;

		return responseCharset;
	}

	/**
	 * Флаг, указывающий на необходимость использования шифрования (SSL) при
	 * выполнении запроса.
	 * 
	 * @return Флаг, указывающий на необходимость использования шифрования (SSL)
	 *         при выполнении запроса.
	 */
	public boolean isSecuredResuest() {
		return securedResuest;
	}

	/**
	 * Адрес сервера для установления соединения.
	 * 
	 * @param host
	 *            Адрес сервера для установления соединения.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Порт сервера для установления соединения.
	 * 
	 * @param port
	 *            Порт сервера для установления соединения.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Кодировка, в которую перекодируются из UTF-8 символы запроса при отправке
	 * на сервер.
	 * 
	 * @param charsetName
	 *            Кодировка, в которую перекодируются из UTF-8 символы запроса
	 *            при отправке на сервер.
	 */
	public void setRequestCharset(Charset charsetName) {
		this.requestCharset = charsetName.toString();
	}

	/**
	 * Кодировка, в которую перекодируются из UTF-8 символы запроса при отправке
	 * на сервер.
	 * 
	 * @param charsetName
	 *            Кодировка, в которую перекодируются из UTF-8 символы запроса
	 *            при отправке на сервер.
	 */
	public void setRequestCharset(String charsetName) {
		this.requestCharset = Charset.forName(charsetName).toString();
	}

	/**
	 * Кодировка, из которой перекодируются в UTF-8 символы ответа при получении
	 * их с сервера.
	 * 
	 * @param charsetName
	 *            Кодировка, из которой перекодируются в UTF-8 символы ответа
	 *            при получении их с сервера.
	 */
	public void setResponseCharset(Charset charsetName) {
		this.responseCharset = charsetName.toString();
	}

	/**
	 * Кодировка, из которой перекодируются в UTF-8 символы ответа при получении
	 * их с сервера.
	 * 
	 * @param charsetName
	 *            Кодировка, из которой перекодируются в UTF-8 символы ответа
	 *            при получении их с сервера.
	 */
	public void setResponseCharset(String charsetName) {
		this.responseCharset = Charset.forName(charsetName).toString();
	}

	/**
	 * Флаг, указывающий на необходимость использования шифрования (SSL) при
	 * выполнении запроса.
	 * 
	 * @param securedResuest
	 *            Флаг, указывающий на необходимость использования шифрования
	 *            (SSL) при выполнении запроса.
	 */
	public void setSecuredResuest(boolean securedResuest) {
		this.securedResuest = securedResuest;
	}

}
