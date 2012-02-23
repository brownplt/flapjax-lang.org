<html>
<head>


<link rel="stylesheet" href="/demo.css"/>
<script type="text/javascript" src="/fx/flapjax.js"></script>

<title>Flapjax Demo: Saving Drafts on the Server</title>

<style type="text/css">
.saved { border: 3px solid green; }
.unsaved { border: 3px solid red; }
</style>

<script type="text/javascript">
// Wraps the draft object into a request.
function save(draft) {
  return { 
    url: '/fxserver/setobj/draft/',
    fields: { savedtext: draft, 
              savetime: (new Date()).toLocaleString() },
    request: 'post', 
    response: 'json',
    async: true }};

function load(ignored_arg) {
  var obj = { savedtext: "",
              savetime: "draft never saved" };
  return { url: '/fxserver/getobj/draft/',
    fields: obj,
    request: 'post', 
    response: 'json',
    async: true  }};
</script>

<script type="text/flapjax">


// autoSave :: String * EventStream save 
//          -> { input: TEXTAREA, savedE: EventStream }
var autoSave = function(initial,saveE) {
  var input = TEXTAREA_(initial);

  var savedE = 
    getWebServiceObjectE(saveE.snapshotE($B(input)).mapE(save));

  return { input: input, savedE : savedE }};

// Save when the Save button is clicked, and every 10 seconds.
var saveE = mergeE(clicksE("saveButton"),
                   timerE(10000));

var initialValueE = getWebServiceObjectE(oneE(true).mapE(load))
                    .mapE(function(x) { return x.savedtext; });

initialValueE.mapE(function(initial) {

  var r = autoSave(initial,saveE);
  insertDomB(r.input,"inputPlaceholder");

  var statusB =
    mergeE($B(r.input).changes().constantE("unsaved"),
           r.savedE.constantE("saved"))
    .startsWith("saved");

  insertValueB(statusB,r.input,"className");

  // When the page loads, load the saved draft

  // For the paranoid, we show what the currently saved draft is, by
  // polling the server!
  var serverDraftB = 
    getWebServiceObjectE(timerE(5000).mapE(load))
    .startsWith({ savedtext : "Please wait ...", savetime: "never saved" });
  
  insertDomB(SPAN(serverDraftB.savedtext),"curDraft");

  insertDomB(SPAN(serverDraftB.savetime),"savedTime");
});
</script>
</head>

<body>

<p>Drafts are associated with your current session and are stored on the
server.  Sessions last for an hour.  Drafts are saved every five seconds and
whenever you click <i>Save Now</i>.  If you reload the page, the draft will be
loaded from the server.</p>

Last Saved: <span id='savedTime'></span>

<form>
<span id="inputPlaceholder"></span><br/>
<input type='button' id='saveButton' value='Save Now'/>
</form>

<p>The currently saved draft is:</p>
<div><span class="block" id="curDraft"></span></div>

</body>
</html>



