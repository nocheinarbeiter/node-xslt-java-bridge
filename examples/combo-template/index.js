var Transformer = require('../../').XsltTransformer;

var t1 = new Transformer(['foo.xsl', 'bar.xsl']);
var t2 = new Transformer(['bar.xsl', 'foo.xsl']);

[t1, t2].forEach(transformer => {
    var results = transformer.transform({
        base: __dirname,
        path: 'main.xml',
        contents: '<main/>'
    });
    console.log(results[0].contents);
});
