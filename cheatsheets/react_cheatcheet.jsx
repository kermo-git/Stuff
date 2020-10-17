/* * * * * * *
 * ELEMENDID *
 * * * * * * */


// Elemendi loomiseks on mitu viisi:

// JSX syntaks
const element = (
    <h1 className="greeting">
      Hello, world!
    </h1>
);

const element = React.createElement(
    'h1',
    {className: 'greeting'},
    'Hello, world!'
);

const element = {
    type: 'h1',
    props: {
      className: 'greeting',
      children: 'Hello, world!'
    }
};

// Oletame, et kuskil HTML'is on element <div id="root"></div>.
// Kuvab elemendi selle sisse.
ReactDOM.render(element, document.getElementById('root'));


/* * * * * * * *
 * KOMPONENDID *
 * * * * * * * */


// Komponente saab luua tavalise JavaScripi fuktsioonina, mis tagastab elemendi (mis võib sisaldada ka teisi komponente)
// või React.Component alamklassina, mille render() meetod tagastab elemendi. Argument props peab olema mingi objekt.

function Welcome(props) {
    // Loogelistes sulgudes võib olla mistahes JavaScripti avaldis.
    return <h1>Hello, {props.name}</h1>;
}

class Welcome extends React.Component {
    render() {
      return <h1>Hello, {this.props.name}</h1>;
    }
}

// Kui komponent on loodud saab seda kasutada nagu mistahes DOM elementi.
// Kõik komponendile antavad atribuuid on kättesaadavad objektist props.
// Näiteks "Sara" on komponendis kui props.name. props objekti sisse saab
// lisada ükskõik mida, kasvõi teisi komponente.
const element = <Welcome name="Sara" />;
// Kirjutab ekraanile "Hello, Sara".
ReactDOM.render(
    element,
    document.getElementById('root')
);

// Komponente saab paigutada ka listidesse.
function NumberList(props) {
    const numbers = props.numbers;
    // Tekitab listi  <li/> elementidest. Igal listis oleval elemendil/komponendil
    // peab olema unikaalne key atribuut. NB! Komponendis ei ole props.key kättesaadav!
    const listItems = numbers.map((number) =>
        <li key={number.toString()}>
            {number}
        </li>
    );
    return (
        <ul>{listItems}</ul>
    );
}
  
const numbers = [1, 2, 3, 4, 5];
ReactDOM.render(
    <NumberList numbers={numbers} />,
    document.getElementById('root')
);


/* * * * * * * * * *
 * KOMPONENDI OLEK *
 * * * * * * * * * */


// Kell, mis näitab sekundeid.
class Clock extends React.Component {
    constructor(props) {
        super(props);
        this.state = {date: new Date()};
    }
  
    // componentDidMount() kutsutakse välja siis, kui komponent DOM'i ilmub. Näiteks
    // siis, kui kutsutakse välja ReactDOM.render().
    componentDidMount() {
        // Lisab komponendile uue property nimega timerID, milleks on funktsioon setInterval(f, x),
        // mis käsib brauseril funktsiooni f iga x ms tagant välja kutsuda.
        this.timerID = setInterval(
            () => this.tick(),
            1000
        );
    }

    tick() {
        // setState() uuendab olekut (this.state) ja kutsub render() meetodit uuesti.
        this.setState({
            date: new Date()
        });
    }

    render() {
        return (
            <div>
                <h1>Hello, world!</h1>
                <h2>It is {this.state.date.toLocaleTimeString()}.</h2>
            </div>
        );
    }
  
    // componentWillUnmount() kutsutakse välja siis, kui komponent DOM'ist eemaldatakse. 
    componentWillUnmount() {
        // Peatab funksiooni setInterval(f, x).
        clearInterval(this.timerID);
    }
}
  
ReactDOM.render(
    <Clock />,
    document.getElementById('root')
);


/* * * * * * *
 * SÜNDMUSED *
 * * * * * * */


// Komponent, mis klikates käivitab funktsiooni handleClick(e)
function ActionLink() {
    function handleClick(event) {
        // preventDefault() ei lase linki avada. Tavalises
        // HTML'is ja JavaScriptis piisaks sellest, kui tagastada false.
        event.preventDefault();
        console.log('The link was clicked.');
    }
  
    return (
        <a href="#" onClick={handleClick}>
            Click me
        </a>
    );
}

// Samasugune asi ES6 klassi kujul:
class ActionLink extends React.Component {
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(event) {
        e.preventDefault();
        console.log('The link was clicked.');
    }

    render() {
        return (
            <a href="#" onClick={this.handleClick}>
                Click me
            </a>
        );
    }
}
