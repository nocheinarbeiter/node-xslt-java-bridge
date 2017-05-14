var Transformer = require('../../').XsltTransformer;

try {
    var compileErrorTransformer = new Transformer('compile-error.xsl');
} catch (e) {
    console.log('*** compile error: ');
    console.log(e.message);
}


var runtimeErrorTransformer = new Transformer('runtime-error.xsl');
var src = { base: __dirname, path: 'divide-by.xml', contents: '<divide number="100" by="0"/>' };
try {
    var res = runtimeErrorTransformer.transform(src);
    console.log(res[0].contents);
} catch (e) {
    console.log('*** runtime error: ');
    console.log(e.message);
}
