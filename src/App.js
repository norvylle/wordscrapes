import React, { Component } from 'react';
import { Menu, Input, Button, Dimmer, Loader  } from 'semantic-ui-react';
import { TagCloud } from 'react-tagcloud';

const autoBind = require('auto-bind');

class App extends Component {
  constructor(props){
    super(props)
    this.state = {
      letters:'',
      search:[],
      data:[],
      loaded:false,
      isloading: false
    }
    autoBind(this)
  }

  async handleLetters(e){
    await this.setState({letters: e.target.value});
    this.setState({search: new Array(this.state.letters.length)})
  }

  async mapMessage(data){
    let list = data.split("\n");
    list.pop();
    for(let i=0; i< list.length;i++){
      list[i] = {value: list[i], count: Math.round( Math.random() * 23 + 12)}
    }
    await this.setState({data: list});
    this.setState({loaded: true});
  }

  handleSearch(e){
    this.setState({search: e.target.value})
  }

  timeout(ms, promise) {
    return new Promise(function(resolve, reject) {
      setTimeout(function() {
        reject(new Error("timeout"))
      }, ms)
      promise.then(resolve, reject)
    })
  }

  handleClick(){
    this.setState({isloading: true, loaded: false});
    this.timeout(12000000, fetch(`http://localhost:3001/?letters=${this.state.letters}&search=${this.state.search}`))
    .then((response) => { return response.json()})
    .then((data) => {this.mapMessage(data[0]); this.setState({isloading: false});})
    .catch((error) => {console.log(error); this.setState({isloading: false});})

  }

  DataCloud(props){
    const loaded = props.loaded;
    if(loaded){
      return(
        <TagCloud minSize={12}
          maxSize={35}
          colorOptions={{hue: 'maroon'}}
          tags={this.state.data} />
      )
    }else if(this.state.isloading){
      return(<Dimmer active>
        <Loader />
      </Dimmer>)
    }
    else{
      return <div/>
    }
  }

  render() {
    return (
      <div>
          <Menu color='red' fixed='top' inverted borderless>
              <Menu.Item header>
              Wordscrapes
              </Menu.Item>
          </Menu>
          <div style={{padding: 50}}/>
          <Input placeholder='Letters' onChange={this.handleLetters} size="big" style={{padding: 10}} />
          <Input placeholder='Search' onChange={this.handleSearch} size="big" style={{padding: 10}} maxLength={this.state.letters.length} />          
          <Button content="Generate" size="big" style={{marginLeft: 10}} onClick={this.handleClick}/>
          <div style={{padding: 50}}/>
          <div style={{alignSelf: "center", padding: 50}}>
          <this.DataCloud loaded={this.state.loaded}/>
          </div>
      </div>
    );
  }
}

export default App;
