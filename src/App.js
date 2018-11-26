import React, { Component } from 'react';
import { Menu, Input, Button  } from 'semantic-ui-react';
const autoBind = require('auto-bind');

class App extends Component {
  constructor(props){
    super(props)
    this.state = {
      letters:'',
      search:[],
      message:''
    }
    autoBind(this)
  }

  async handleLetters(e){
    await this.setState({letters: e.target.value});
    this.setState({search: new Array(this.state.letters.length)})
  }


  handleSearch(e){
    this.setState({search: e.target.value})
  }

  handleClick(){
    fetch(`http://localhost:3001/?letters=${this.state.letters}&search=${this.state.search}`)
    .then((response) => { return response.json()})
    .then((data) => {this.setState({message:data[0]})})
  }

  render() {
    return (
      <div>
          <Menu color='red' fixed='top' inverted borderless>
              <Menu.Item color='white' header inverted>
              Wordscrapes
              </Menu.Item>
          </Menu>
          <div style={{padding: 50}}/>
          <Input placeholder='Letters' onChange={this.handleLetters} maxLength="29"/>
          <Input placeholder='Search' onChange={this.handleSearch} maxLength={this.state.letters.length}/>          
          <Button content="Check State" onClick={this.handleClick}/>
          <div style={{padding: 50}}/>
          {this.state.message}
      </div>
    );
  }
}

export default App;
