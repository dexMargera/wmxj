# Введение #

Java-версия библиотеки для работы с [XML-интерфейсами](http://wiki.webmoney.ru/wiki/show/XML-интерфейсы) WebMoney Transfer. На данный момент содержит все интерфейсы X1-X19, кроме X12 (_Импорт выписки по кошельку в Документ программ семейства "1С Предприятие 7.7"_). Библиотека проверена на Classic & Light версиях киперов (за исключением интерфейса X18 - нужен доступ к WebMoney Merchant, которого у меня пока нет).

# Использование #

Пример вызова сервиса по схеме Classic для отправки сообщения на wmid "854579556367" через интерфейс X6 (ключ для генерации подписи от имени wmid "933269435359" находится в [kwm-файле](http://wiki.flancer.lv/dokuwiki/doku.php?id=pub:howto:wm:kwm_export) на диске):

```
try {
	WmService service = new WmService();
	service.initWmSignerKwm("933269435359",
			"/path/to/933269435359.kwm",
			"KwmAccessPassword");
	X6Response resp = service.x6("854579556367", "Message Subj", "Message body...");
	System.out.println(service.getHttpRequest());
	System.out.println(service.getHttpResponse());
	System.out.println("Response error code:\t"+ resp.getRetVal());
} catch (Exception e) {
	e.printStackTrace();
}
```


Пример вызова сервиса по схеме Light для отправки сообщения на wmid "854579556367" через интерфейс X6 (персональный сертификат и закрытый ключ WebMoney Light находится в [wm.jks](http://wiki.flancer.lv/dokuwiki/doku.php?id=pub:prj:wmxj:lightkeepersetup)):

```
try {
	WmService service = new WmService();
	service.initWmLightKeyStore("/path/to/javaKeyStore/wm.jks", "jksPassword");
	service.setAllowUnsafeRenegotiation(true);
	X6Response resp = service.x6("854579556367", "Message Subj", "Message body...");
	System.out.println(service.getHttpRequest());
	System.out.println(service.getHttpResponse());
	System.out.println("Response error code:\t"+ resp.getRetVal());
} catch (Exception e) {
	e.printStackTrace();
}
```

Более детально о структуре библиотеки - [javadoc](http://flancer.lv/projects/wmxj/javadoc/).



# История возникновения #

С XML интерфейсами Webmoney работаю довольно давно, имею практический опыт применения собственных наработок по интерфейсам X2, X3, X8, X9 (другие в Java не использовал). Недавно появилась необходимость пересмотреть свои наработки и сформировать пакет для работы с XML-интерфесами WebMoney в Java.

В открытом доступе обнаружил проект [webmoneyjava](http://code.google.com/p/webmoneyjava/), использующий в качестве WmSigner'а библиотеку [wmsignerjx](http://code.google.com/p/wmsignerjx/). При попытке использовать предлагаемый функционал, выяснил, что с документацией по проектам не очень хорошо - мне, как "свежему" пользователю было непонятно, каким образом работать с Keeper Classic, да и не все интерфейсы были реализованы (отсутствуют X12, X16-X19). В процессе критического анализа предлагаемых библиотеками возможностей сначала появилась своя версия WmSigner'а (отдельное спасибо Дмитрию Копаеву и его [WMXI](http://my-tools.net/wmxi/)), а затем и своя версия библиотеки по работе с XML-интерфесами WebMoney - WMXJ.