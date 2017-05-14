var Transformer = require('../../').XsltTransformer;
var transformer = new Transformer('main.xsl');

var results = transformer.transform({
    base: __dirname,
    path: 'main.xml',
    contents: '<main/>'
});

console.log(results[0].contents);
