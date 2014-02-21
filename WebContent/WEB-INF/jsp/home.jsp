<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html ng-app="easyLaw" xmlns:fb="http://ogp.me/ns/fb#">
<head>
<title>Easy Law</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/core.css" media="screen"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/handheld.css" media="handheld, only screen and (max-device-width:480px)"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/angularui/angular-ui.min.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jqueryui/jquery-ui-1.10.3.custom.min.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/fineuploader/fineuploader-3.5.0.css"/>
<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jScrollPane.css"/>  -->
<meta name="viewport" content="width=device-width" />
<script>
    var baseUrl = '<%=request.getContextPath()%>';
</script>
<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
<!-- <script src="<%=request.getContextPath()%>/resources/js/jquery.masonary.min.js"></script>  -->
<script src="<%=request.getContextPath()%>/resources/js/angular.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/angular-resource.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/angular-sanitize.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/html5.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jsDate.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/easyLaw.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/base.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/home.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/login.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/signup.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/askquestion.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/profile.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/user.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/edit.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/timeline.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/profileitem.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/lawyers.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/articles.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/question.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/customerquestions.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/lawyerquestions.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/lawyersettings.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/customersettings.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/answer.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/usersettings.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/book.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/customerfiles.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/controller/file.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jqueryui/jquery-ui-1.10.3.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/angularui/angular-ui.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/fineuploader/jquery.fineuploader-3.5.0.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jquery.caret.1.02.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/jquery.scrollTo-1.4.3.1-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/ckeditor/ckeditor.js"></script>
<!-- <script src="<%=request.getContextPath()%>/resources/js/fineuploader/iframe.xss.response-3.5.0.js"></script>  -->
<!-- <script src="<%=request.getContextPath()%>/resources/js/jScrollPane.min.js"></script>  -->
<script src="<%=request.getContextPath()%>/resources/js/jquery.mousewheel.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/fonts/MyFontsWebfontsKit.js"></script>

<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/resources/images/favicon.png"/>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-41082944-1', 'easylaw.com.au');
  ga('send', 'pageview');

</script>
<meta name="google-site-verification" content="WkN_UGJVN24iSEcu5z--I_w6Kt3vf_Zu96fSQ8_VAyA" />
</head>
<body ng-controller="BaseCtrl">
<div class="tooltip"><div class="image"></div><div class="text"></div></div>
<div class="wrapper">

<div class="content">
<header>
    <div class="content-width">
        <a id="header-logo" href="<%=request.getContextPath()%>/#!/"><img class="logo" ng-src="<%=request.getContextPath()%>/resources/images/logo.png"></img></a>
	    <nav>
	        <ul>
	            <li class="first"><a href="#!/question/ask">Ask a Question</a></li>
	            <li><a href="#!/lawyers">Lawyers</a></li>
	            <li><a href="#!/articles">Articles</a></li>
	            <li><a href="#!/aboutus">About Us</a></li>
	            <li><a href="#!/lawyersjoinus">Lawyers Join Us</a></li>
	            <li ng-show="!user.success"class="last"><a href="#!/login">Login</a></li>
	            <li ng-show="user.success && user.lawyer" class="last"><a href="#!/lawyer/profile">My Account</a></li>
	            <li ng-show="user.success && !user.lawyer" class="last"><a href="#!/customer/questions">My Account</a></li>
	        </ul>
	    </nav>
    </div>
</header>
<div ng-view></div>
</div><!-- content -->

<div class="footer">
    <footer class="content-width">
        <nav>
            <ul>
                <li ng-show="user.success"><a href="<%=request.getContextPath()%>/j_spring_security_logout">Log Out</a></li>
                <!-- <li><a href="#!/faq">FAQ</a></li>  -->
                <li><a href="#!/aboutus">About Us</a></li>
                <li><a href="#!/contactus">Contact Us</a></li>
                <li><a href="#!/articles">Articles</a></li>
                <li><a href="#!/privacy">Privacy</a></li>
                <li class="last"><a href="#!/terms">Terms of Use</a></li>
            </ul>
        </nav>
        <a href="http://www.facebook.com/easylawhq" target="_blank" class='facebook-logo'></a>
        <a href="http://www.twitter.com/easylawhq"  target="_blank" class='twitter-logo'></a>
        <a target="_blank" class='linkedin-logo'></a>
        <span id="some-element"></span>
    </footer>
</div>

</div><!-- wrapper -->
</body>
</html>