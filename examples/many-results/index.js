var Transformer = require('../../').XsltTransformer;
var transformer = new Transformer('results.xsl');

var results = transformer.transform({
    base: '/base',
    path: 'some-path/main.xml',
    contents: '<main/>'
});

results.forEach(({path, contents}) => {
    console.log(`${path} | ${contents}`);
});
